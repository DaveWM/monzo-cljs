(ns monzo-cljs.db
  (:require [datascript.core :as d]
            [cemerick.url :refer [url]]
            [cljs.reader :as reader]
            [cljs-time.core :refer [now]]
            [cljs-time.coerce :refer [to-string]]))

(def app-datom-id 1)
(def local-storage-key "state")

(defn get-url-params []
  (-> js/window
      .-location
      .-href
      url
      :query))

(defn get-local-storage []
  (->> (.-length js/localStorage)
       (range)
       (map #(.key js/localStorage %))
       (map #(vec [% (.getItem js/localStorage %)]))
       (into {})))

(defn get-app-db []
  (let [db (-> (if-let [serialized-state (-> (.getItem js/localStorage local-storage-key))]
                 (reader/read-string serialized-state)
                 (d/db-with (d/empty-db) [[:db/add app-datom-id :app/title "Monzo Web"]
                                          [:db/add app-datom-id :transactions/selected-group :date]
                                          [:db/add app-datom-id :transactions/selected-sort :default]
                                          [:db/add app-datom-id :transactions/sort-direction true]
                                          [:db/add app-datom-id :transactions/selected-limit :month]
                                          [:db/add app-datom-id :transactions/loading false]]))
               d/conn-from-db)]
    (d/transact! db [[:db/add app-datom-id :app/current-date (to-string (now))]])
    db))

(defn save-app-db [db]
  (.setItem js/localStorage local-storage-key (pr-str db)))
