(ns monzo-cljs.events
  (:require [cljs.core.async :refer [chan <! put! pipeline]]
            [monzo-cljs.auth :refer [oauth-url check-token-valid exchange-auth-code]]
            [monzo-cljs.routing :refer [get-route-url]]
            [posh.reagent :refer [transact!]]
            [datascript.core :as d]
            [monzo-cljs.db :refer [app-datom-id]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn transduce-chan [from transducer]
  (let [out (chan)]
    (pipeline 1 out transducer from)
    out))

(defmulti process-event first)
(defmethod process-event :default [] nil)

(defmethod process-event :routes/root [event db]
  (let [{token :ls/token} (d/pull db [:ls/token] app-datom-id)]
    [nil (fn [_ {:keys [http-get]}]
           (-> (check-token-valid token http-get)
               (transduce-chan (map #(vec [:auth/token-checked (get-in % [:body :authenticated])])))))]))

(defmethod process-event :auth/token-checked [[_ token-valid?] db]
  (when-not token-valid?
    [nil (fn [_ {:keys [window]}]
           (set! (.-href (.-location window))
                 oauth-url))]))

(defmethod process-event :routes/oauth [[_ {:keys [code]}] db]
  [nil (fn [_ {:keys [http-post]}]
         (-> (exchange-auth-code code http-post)
             (transduce-chan (map #(vec [:auth/token-received (get-in % [:body :access_token])])))))])

(defmethod process-event :auth/token-received [[_ token] db]
  [[[:db/add app-datom-id :ls/token token]]
   (fn [_ {:keys [local-storage window]}]
     (.setItem local-storage "token" token)
     (set! (.-href (.-location window))
                 (get-route-url :routes/root)))])


(defn start-event-loop [event-chan app-db dependencies]
  (go-loop []
    (let [event (<! event-chan)
          current-db @app-db
          [db-update command] (process-event event current-db)]
      (println "event: " event)
      (when-let [command-chan (and command (command app-db dependencies))]
        (->> (<! command-chan)
             (put! event-chan)))
      (when db-update
        (transact! app-db db-update))
      (recur))))
