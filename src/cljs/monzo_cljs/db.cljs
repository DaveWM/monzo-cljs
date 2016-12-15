(ns monzo-cljs.db
  (:require [posh.reagent :refer [pull q posh! transact!]]
            [datascript.core :as d]
            [cemerick.url :refer [url]]))

(def app-datom-id 1)

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
  (let [app-db (d/create-conn)]
    (posh! app-db)
    
    (transact! app-db [[:db/add app-datom-id :app/title "Monzo Web"]])
    
    (transact! app-db (->> (get-local-storage)
                           (map (fn [[ls-key ls-value]]
                                  [:db/add app-datom-id (keyword "ls" ls-key) ls-value]))))

    app-db))
