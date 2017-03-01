(ns monzo-cljs.events.routes
  (:require [datascript.core :as d]
            [monzo-cljs.api :refer [get-accounts]]
            [monzo-cljs.auth :refer [check-token-valid exchange-auth-code]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.events.core :refer [process-event]]
            [monzo-cljs.utilities :refer [success? transduce-chan]]
            [cljs.core.async :refer [merge]]))

(defmethod process-event :routes/home [[route] db]
  (let [{token :auth/token} (d/pull db [:auth/token] app-datom-id)]
    [[[:db/add app-datom-id :routes/current route]]
     (fn [_ {:keys [http-get]}]
       (merge [(-> (check-token-valid token http-get)
                   (transduce-chan (map #(vec [:auth/token-checked
                                               (and (not= (:status %) 401)
                                                    (get-in % [:body :authenticated]))
                                               (zero? (:status %))]))))
               (-> (get-accounts token http-get)
                   (transduce-chan (comp (filter success?)
                                         (map #(vec [:api/accounts-retrieved (get-in % [:body :accounts])])))))]))]))



(defmethod process-event :routes/oauth [[route {:keys [code]}] db]
  [[[:db/add app-datom-id :routes/current route]]
   (fn [_ {:keys [http-post]}]
         (-> (exchange-auth-code code http-post)
             (transduce-chan (map #(vec [:auth/token-received (select-keys (:body %) [:access_token :refresh_token])])))))])
