(ns monzo-cljs.api
  (:require [cemerick.url :refer [url]]))

(defn auth-header [token]
  {"Authorization" (str "Bearer " token)})

(defn get [url http-get token]
  "makes an authenticated GET request to the given url (either a string or a cemerick.url url)"
  (http-get (str url) {:headers (auth-header token)}))

(defn get-transactions [account-id token http-get]
  (get (-> (url "https://api.monzo.com/transactions")
           (assoc :query {"account_id" account-id
                          "expand[]" "merchant"}))
       http-get token))

(defn get-accounts [token http-get]
  (get "https://api.monzo.com/accounts" http-get token))
