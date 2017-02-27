(ns monzo-cljs.db
  (:require [datascript.core :as d]
            [cemerick.url :refer [url]]
            [cljs.reader :as reader]
            [cljs-time.core :refer [now]]
            [cljs-time.coerce :refer [to-string]]))

(def app-datom-id 1)
(def local-storage-key "state")

(defn get-local-storage []
  (->> (.-length js/localStorage)
       (range)
       (map #(.key js/localStorage %))
       (map #(vec [% (.getItem js/localStorage %)]))
       (into {})))

(def version 1)
(def initial-data {:app/title "Monzo Web"
                   :transactions/selected-group :date
                   :transactions/selected-sort :default
                   :transactions/sort-direction true
                   :transactions/selected-limit :month
                   :transactions/loading false})

(defn get-initial-transactions [data-map version]
  (->> data-map
       (merge {:db/version version})
       (map (fn [[k v]] [:db/add app-datom-id k v]))
       vec))

(defn get-app-db []
  (let [saved-db (when-let [serialized-state (-> (.getItem js/localStorage local-storage-key))]
                   (reader/read-string serialized-state))
        saved-db-version (if saved-db
                             (-> saved-db
                                 (d/pull [:db/version] app-datom-id)
                                 :db/version)
                             -1)
        db (-> (if (and saved-db (= saved-db-version version))
                 saved-db
                 (d/db-with (d/empty-db) (get-initial-transactions initial-data version)))
               d/conn-from-db)]
    (d/transact! db [[:db/add app-datom-id :app/current-date (to-string (now))]])
    db))

(defn save-app-db [db local-storage]
  (.setItem local-storage local-storage-key (pr-str db)))
