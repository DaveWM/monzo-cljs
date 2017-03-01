(ns monzo-cljs.events.home-page
  (:require [cljs.core.async :refer [merge to-chan]]
            [datascript.core :as d]
            [monzo-cljs.api :refer [get-balance get-transactions]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.events.core :refer [process-event]]
            [monzo-cljs.utilities
             :refer
             [convert-date-keys
              map-to-update
              monzo-id-to-int
              namespace-keys
              success?
              transduce-chan]]))

(defn get-last-transaction-id [db]
  (->> (d/q '[:find ?id ?date
              :where
              [?e :transaction/created ?date]
              [?e :transaction/id ?id]]
            db)
       (sort-by second)
       reverse
       ffirst))

(defn transaction-response-to-event-chan [response-chan db last-transaction-id account-id]
  (transduce-chan response-chan
                  (comp (filter success?)
                        (map #(vec [:api/transactions-retrieved (get-in % [:body :transactions]) account-id])))))

(defmethod process-event :api/accounts-retrieved [[_ accounts] db]
  [(->> accounts
        (mapcat (fn [{:keys [id description created]}]
                  (let [int-id (monzo-id-to-int id)]
                    [[:db/add int-id :account/description description]
                     [:db/add int-id :account/created created]
                     [:db/add int-id :account/monzo-id id]]))))
   (fn [db]
     (when-let [account-id (-> (d/q '[:find ?e
                                      :where [?e :account/description]]
                                    db)
                               ffirst)]
       (to-chan [[:action/account-selected account-id]])))])

(defmethod process-event :api/balance-retrieved [[_ response account-id] db]
  [(->> response
        (namespace-keys "balance")
        (map-to-update account-id))])

(defmethod process-event :api/transactions-retrieved [[_ transactions account-id] db]
  [(conj (->> transactions
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
                                       (map-to-update merchant-id)))))))
         [:db/add app-datom-id :transactions/loading false])])

(defmethod process-event :action/account-selected [[_ account-id] db]
  (let [{token :auth/token} (d/pull db [:auth/token] app-datom-id)]
    [[[:db/add app-datom-id :app/selected-account account-id]
      [:db/add app-datom-id :transactions/loading true]]
     (fn [db {:keys [http-get]}]
       (let [{monzo-account-id :account/monzo-id} (d/pull db '[:account/monzo-id] account-id)
             last-transaction-id (get-last-transaction-id db)]
         (merge
          [(-> (get-transactions monzo-account-id last-transaction-id token http-get)
               (transaction-response-to-event-chan db last-transaction-id account-id))
           (-> (get-balance monzo-account-id token http-get)
               (transduce-chan (comp (filter success?)
                                     (map #(vec [:api/balance-retrieved (:body %) account-id])))))])))]))

(defmethod process-event :action/refresh-transactions [_ db]
  (println "here")
  (let [{token :auth/token} (d/pull db [:auth/token] app-datom-id)
        last-transaction-id (get-last-transaction-id db)
        {selected-account-id :app/selected-account} (d/pull db [:app/selected-account] app-datom-id)
        {monzo-selected-account-id :account/monzo-id} (d/pull db [:account/monzo-id] selected-account-id)]
    [[[:db/add app-datom-id :transactions/loading true]]
     (fn [db {:keys [http-get]}]
       (-> (get-transactions monzo-selected-account-id last-transaction-id token http-get)
           (transaction-response-to-event-chan db last-transaction-id selected-account-id)))]))

(defmethod process-event :action/select-transaction-grouping [[_ group-type] db]
  [[[:db/add app-datom-id :transactions/selected-group group-type]]])

(defmethod process-event :action/select-transaction-sorting [[_ sort-type] db]
  [[[:db/add app-datom-id :transactions/selected-sort sort-type]
    [:db/add app-datom-id :transactions/sort-direction true]]])

(defmethod process-event :action/change-transaction-sort-direction [_ db]
  (let [{:keys [transactions/sort-direction]} (d/pull db [:transactions/sort-direction] app-datom-id)]
    [[[:db/add app-datom-id :transactions/sort-direction (not sort-direction)]]]))

(defmethod process-event :action/select-transaction-limit [[_ limit] db]
  [[[:db/add app-datom-id :transactions/selected-limit limit]]])
