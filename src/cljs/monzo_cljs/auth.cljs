(ns monzo-cljs.auth
  (:require [cemerick.url :refer [url]]
            [monzo-cljs.credentials :refer [credentials]]
            [cljs.core.async :refer [chan put! pipeline]]
            [monzo-cljs.routing :refer [oauth-route]]
            [datascript.core :as d]
            [monzo-cljs.credentials :refer [credentials]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def redirect-url (let [current-domain-url (-> js/window
                                               .-location
                                               .-href
                                               url
                                               (assoc :path nil)
                                               (assoc :query nil))]
                    (-> (apply url current-domain-url oauth-route)
                        str)))

(def oauth-url (-> (url "https://auth.getmondo.co.uk")
                   (assoc :query {:client_id (:client-id credentials)
                                  :redirect_uri redirect-url
                                  :response_type "code"})
                   str))

(def token-endpoint "https://monzo-proxy.herokuapp.com/oauth2/token")

(defn check-token-valid [token http-get]
  "Checks whether the given token is still valid by pinging the /whoami endpoint on the monzo api. Returns a channel."
  (http-get "https://api.monzo.com/ping/whoami" {:headers {"Authorization" (str "Bearer " token)}}))

(defn exchange-auth-code [code http-post]
  (http-post token-endpoint {:form-params {:grant_type "authorization_code"
                                           :client_id (:client-id credentials)
                                           :redirect_uri redirect-url
                                           :code code}
                             :with-credentials? false}))

(defn refresh-access-code [refresh-token http-post]
  (http-post token-endpoint {:form-params {:grant_type "refresh_token"
                                           :client_id (:client-id credentials)
                                           :refresh_token refresh-token}
                             :with-credentials? false}))
