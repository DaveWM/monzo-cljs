(ns monzo-cljs.events.auth
  (:require [cljs.core.async :refer [chan to-chan]]
            [datascript.core :as d]
            [monzo-cljs.auth :refer [oauth-url refresh-access-code]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.events.core :refer [process-event]]
            [monzo-cljs.routing :refer [get-route-url]]
            [monzo-cljs.utilities :refer [transduce-chan]]))

(defmethod process-event :auth/token-checked [[_ token-valid? offline?] db]
  (when (and (not offline?) (not token-valid?))
    (let [{:keys [auth/refresh-token]} (d/pull db [:auth/refresh-token] app-datom-id)]
      [nil (fn [_ {:keys [http-post]}]
             (-> (refresh-access-code refresh-token http-post)
                 (transduce-chan (map #(vec [:auth/token-refreshed (select-keys (:body %) [:access_token :refresh_token])])))))])))

(defmethod process-event :auth/token-refreshed [[_ {access-token :access_token refresh-token :refresh_token}] db]
  (let [{current-route :routes/current} (d/pull db [:routes/current] app-datom-id)]
    [(when access-token
       [[:db/add app-datom-id :auth/token access-token]
        [:db/add app-datom-id :auth/refresh-token refresh-token]])
     
     (if access-token
       #(to-chan [[current-route]])
       (fn [_ {:keys [window]}]
         (set! (.-href (.-location window))
               oauth-url)
         (chan)))]))

(defmethod process-event :auth/token-received [[_ {access-token :access_token refresh-token :refresh_token}] db]
  [[[:db/add app-datom-id :auth/token access-token]
    [:db/add app-datom-id :auth/refresh-token refresh-token]]
   (fn [_ {:keys [window]}]
     (set! (.-href (.-location window))
           (get-route-url :routes/home))
     (chan))])
