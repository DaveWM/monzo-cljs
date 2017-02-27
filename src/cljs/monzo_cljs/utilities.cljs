(ns monzo-cljs.utilities
  (:require [monzo-cljs.currencies :refer [currencies]]
            [cljs.core.async :refer [chan pipeline]]
            [goog.string :as gstring]
            [goog.string.format]))

(defn format-amount [currency amount]
  (let [symbol (-> (keyword currency)
                   currencies
                   :symbol_native)
        sign (when (> 0 amount) "-")]
    (gstring/format (str sign symbol "%.2f") (->> (/ amount 100)
                                                  (.abs js/Math)))))

(defn transduce-chan [from transducer]
  (let [out (chan)]
    (pipeline 1 out transducer from)
    out))

(defn namespace-keys [namespace m]
  (->> m
       (map (fn [[k v]]
                [(keyword namespace (name k)) v]))
       (into {})))

(defn map-to-update [id m]
  (->> m
       (filter #(not (nil? (second %))))
       (map (fn [[k v]]
              [:db/add id k v]))))

(defn convert-date-keys [keys m]
  (reduce (fn [result k]
            (update result k #(js/Date. %)))
          m keys))

(defn success? [response]
  (>= 299 (:status response) 200))

(defn snake-case-to-capitalised [s]
  (when s
    (->> (split s #"_")
         (map capitalize)
         (join " "))))
