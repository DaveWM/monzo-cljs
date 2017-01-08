(ns monzo-cljs.events
  (:require [cljs.core.async :refer [chan <! put! pipeline pipe merge to-chan]]
            [monzo-cljs.auth :refer [oauth-url check-token-valid exchange-auth-code]]
            [monzo-cljs.routing :refer [get-route-url]]
            [datascript.core :as d]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.api :refer [get-accounts get-transactions get-balance]]
            [clojure.string :refer [split]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn transduce-chan [from transducer]
  (let [out (chan)]
    (pipeline 1 out transducer from)
    out))

(defn namespace-keys [namespace m]
  (->> m
       (map (fn [[k v]]
                [(keyword namespace (name k)) v]))
       (into {})))

(defn map-to-update [id m]
  (->> m
       (filter second)
       (map (fn [[k v]]
              [:db/add id k v]))))

(defn convert-date-keys [keys m]
  (reduce (fn [result k]
            (update result k #(js/Date. %)))
          m keys))

(defmulti process-event first)
(defmethod process-event :default [] nil)

(defmethod process-event :routes/home [[route] db]
  (let [{token :auth/token} (d/pull db [:auth/token] app-datom-id)]
    [[[:db/add app-datom-id :routes/current route]]
     (fn [_ {:keys [http-get]}]
       (merge [(-> (check-token-valid token http-get)
                    (transduce-chan (map #(vec [:auth/token-checked (and (not= (:status %) 401)
                                                                         (get-in % [:body :authenticated]))]))))
               (-> (get-accounts token http-get)
                   (transduce-chan (map #(vec [:api/accounts-retrieved (get-in % [:body :accounts])]))))]))]))

(defmethod process-event :auth/token-checked [[_ token-valid?] db]
  (when-not token-valid?
    [nil (fn [_ {:keys [window]}]
           (set! (.-href (.-location window))
                 oauth-url)
           (chan))]))

(defmethod process-event :routes/oauth [[route {:keys [code]}] db]
  [[[:db/add app-datom-id :routes/current route]]
   (fn [_ {:keys [http-post]}]
         (-> (exchange-auth-code code http-post)
             (transduce-chan (map #(vec [:auth/token-received (get-in % [:body :access_token])])))))])

(defmethod process-event :auth/token-received [[_ token] db]
  [[[:db/add app-datom-id :auth/token token]]
   (fn [_ {:keys [window]}]
     (set! (.-href (.-location window))
           (get-route-url :routes/home))
     (chan))])

(defn monzo-id-to-int [monzo-id]
  (js/Math.abs (hash monzo-id)))

(defmethod process-event :api/accounts-retrieved [[_ accounts] db]
  [(->> accounts
        (mapcat (fn [{:keys [id description created]}]
                  (let [int-id (monzo-id-to-int id)]
                    [[:db/add int-id :account/description description]
                     [:db/add int-id :account/created created]]))))
   #(to-chan [[:action/account-selected (first accounts)]])])

(defmethod process-event :action/account-selected [[_ account] db]
  (let [{token :auth/token} (d/pull db [:auth/token] app-datom-id)
        account-id (-> account
                       :id
                       monzo-id-to-int)]
    [[[:db/add app-datom-id :app/selected-account account-id]]
     (fn [_ {:keys [http-get]}]
       (let [{monzo-account-id :id} account]
         (merge
          [(-> (get-transactions monzo-account-id token http-get)
               (transduce-chan (map #(vec [:api/transactions-retrieved (get-in % [:body :transactions]) account-id]))))
           (-> (get-balance monzo-account-id token http-get)
               (transduce-chan (map #(vec [:api/balance-retrieved (:body %) account-id]))))])))]))

(defmethod process-event :api/balance-retrieved [[_ response account-id] db]
  [(->> response
        (namespace-keys "balance")
        (map-to-update account-id))])

(defmethod process-event :api/transactions-retrieved [[_ transactions account-id] db]
  [(->> transactions
        (mapcat (fn [{:keys [id] :as transaction}]
                  (let [db-id (monzo-id-to-int id)
                        merchant-id (-> transaction
                                        (get-in [:merchant :id])
                                        monzo-id-to-int)]
                    (concat (as-> transaction t
                              (assoc t :transaction/account-id account-id)
                              (assoc t :transaction/merchant-id merchant-id)
                              (dissoc t :merchant)
                              (convert-date-keys [:created] t)
                              (namespace-keys "transaction" t)
                              (map-to-update db-id t))
                            (->> (:merchant transaction)
                                 (namespace-keys "merchant")
                                 (map-to-update merchant-id)))))))])


(defn start-event-loop [event-chan app-db dependencies]
  (go-loop []
    (let [event (<! event-chan)
          [db-update command] (process-event event @app-db)]
      ;(println "event: " event)
      (when-let [command-chan (and command (command @app-db dependencies))]
        (pipe command-chan event-chan false))
      (when db-update
        (d/transact! app-db db-update))
      (recur))))
