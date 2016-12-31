(ns monzo-cljs.utilities
  (:require [monzo-cljs.currencies :refer [currencies]]
            [goog.string :as gstring]
            [goog.string.format]))

(defn format-amount [currency amount]
  (let [symbol (-> (keyword currency)
                   currencies
                   :symbol_native)
        sign (when (> 0 amount) "-")]
    (gstring/format (str sign symbol "%.2f") (->> (/ amount 100)
                                                  (.abs js/Math)))))
