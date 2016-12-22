(ns monzo-cljs.home-page
  (:require [posh.reagent :refer [pull q]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.currencies :refer [currencies]]
            [goog.string :as gstring]
            [goog.string.format]
            [cljs-time.coerce :as coerce-time]
            [cljs-time.core :as time]))

(defn format-amount [amount currency]
  (let [symbol (-> (keyword currency)
                   currencies
                   :symbol_native)
        sign (when (> 0 amount) "-")]
    (gstring/format (str sign symbol "%.2f") (->> (/ amount 100)
                                                  (.abs js/Math)))))

(defn home-page [app-db]
  (let [data @(q '[:find ?e ?created ?amount ?desc ?currency
                   :where
                   [?e :transaction/description ?desc]
                   [?e :transaction/amount ?amount]
                   [?e :transaction/currency ?currency]
                   [?e :transaction/created ?created]]
                 app-db)]
    (if (empty? data)
      
      [:div {:class "mdl-spinner is-active"}]
      
      [:div {:class "home-card mdl-card mdl-shadow--2dp"}
       [:div {:class "mdl-card__title"}
        [:h2 {:class "mdl-card__title-text"} "Transactions"]]
       [:div {:class "mdl-card__supporting-text"}
        [:ul {:class "mdl-list"}
         (->> data
              (sort-by second >)
              (map (fn [[id created amount desc currency]]
                     ^{:key id}
                     [:li {:class "mdl-list__item"}
                      [:span {:class "mdl-list__item-primary-content"}
                       (str (format-amount amount currency) " " desc)]])))]]])))
