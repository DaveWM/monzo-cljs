(ns monzo-cljs.auth
  (:require [cemerick.url :refer [url]]
            [monzo-cljs.credentials :refer [credentials]]
            [cljs.core.async :refer [chan put! pipeline]]
            [monzo-cljs.routing :refer [oauth-route]]
            [datascript.core :as d]
            [monzo-cljs.credentials :refer [credentials]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def redirect-url (str (apply url "http://localhost:8000" oauth-route)))

(def oauth-url (-> (url "https://auth.getmondo.co.uk")
                      (assoc :query {:client_id (:client-id credentials)
                                     :redirect_uri redirect-url
                                     :response_type "code"})
                      str))

(defn check-token-valid [token http-get]
  "Checks whether the given token is still valid by pinging the /whoami endpoint on the monzo api. Returns a channel."
  (http-get "https://api.monzo.com/ping/whoami" {:headers {"Authorization" (str "Bearer " token)}}))

(defn exchange-auth-code [code http-post]
  (http-post "https://api.monzo.com/oauth2/token" {:form-params {:grant_type "authorization_code"
                                                                 :client_id (:client-id credentials)
                                                                 :client_secret (:client-secret credentials)
                                                                 :redirect_uri redirect-url
                                                                 :code code}}))
