(ns monzo-cljs.home-page
  (:require [datascript.core :refer [pull q]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.currencies :refer [currencies]]
            [goog.string :as gstring]
            [goog.string.format]
            [cljs-time.coerce :as coerce-time]
            [cljs-time.core :as time :refer [year month day]]
            [cljs-time.format :as time-format]
            [clojure.string :refer [blank?]]
            [datascript.core :as d]))

(defn format-amount [currency amount]
  (let [symbol (-> (keyword currency)
                   currencies
                   :symbol_native)
        sign (when (> 0 amount) "-")]
    (gstring/format (str sign symbol "%.2f") (->> (/ amount 100)
                                                  (.abs js/Math)))))

(def date-format (time-format/formatter "dd MMMM yyyy"))
(def date-time-format (time-format/formatter "dd/MM/yyyy HH:mm"))

(defn format-date [date]
  (time-format/unparse date-format date))

(defn format-date-time [date]
  (time-format/unparse date-time-format date))

(defn sum-transactions [transactions]
  (->> transactions
       (reduce (fn [sum [_ _ amount]]
                 (+ sum amount))
               0)))

(defn get-transactions-currency [transactions]
  "returns the common currency for a list of transactions, or nil if the transactions are in different currencies"
  (let [currencies (->> transactions
                        (map #(nth % 4))
                        (into #{}))]
    (when (= 1 (count currencies))
      (first currencies))))

(defn home-page [app-db]
  (let [data (->> (q '[:find ?e ?created ?amount ?desc ?currency ?m
                        :in $
                        :where
                        [?e :transaction/description ?desc]
                        [?e :transaction/amount ?amount]
                        [?e :transaction/currency ?currency]
                        [?e :transaction/created ?created]
                        [(get-else $ ?e :transaction/merchant-id false) ?m]]
                      app-db)
                  (map (fn [transaction]
                         (let [m-id (last transaction)]
                           {:transaction transaction
                            :merchant (when m-id
                                        (-> (d/pull app-db '[:merchant/emoji :merchant/logo :merchant/name :merchant/address]
                                                   m-id)
                                            ((juxt :merchant/emoji :merchant/logo :merchant/name :merchant/address))))}))))]
    (if (empty? data)
      
      [:div {:class "mdl-spinner is-active"}]
      
      [:div {:class "home-card mdl-card mdl-shadow--2dp"}
       [:div {:class "home-card__title mdl-card__title"}
        [:h2 {:class "mdl-card__title-text"} "Transactions"]]
       [:div {:class "mdl-card__supporting-text"}
        (->> data
             (group-by (comp (juxt year month day)
                             second
                             :transaction))
             (sort-by first)
             reverse
             (map (fn [[date-parts day-data]]
                    (let [day-transactions (map :transaction day-data)]
                      ^{:key date-parts}
                      [:div {:class "group"}
                       [:h4 {:class "group__header"} (format-date (apply time/date-time date-parts))]
                       (let [sum (sum-transactions day-transactions)
                             currency (get-transactions-currency day-transactions)]
                         (when currency
                           [:h5 {:class (str "group__sub-header "
                                             (if (pos? sum)
                                               "group__sub-header--positive"
                                               "group__sub-header--negative"))}
                            (str " " (format-amount currency sum))]))
                       [:ul {:class "mdl-list"}
                        (->> day-data
                             (sort-by (comp second :transaction) <)
                             (map (fn [{[id created amount desc currency] :transaction
                                        [icon logo merchant address] :merchant}]
                                    (let [is-credit (pos? amount)]
                                      ^{:key id}
                                      [:li {:class (str "transaction mdl-list__item "
                                                        (if is-credit "transaction--credit" "transaction--debit"))}
                                       (if (not (blank? logo))
                                         [:img {:class "mdl-list__item-icon transaction__icon"
                                                :src logo}]
                                         [:span {:class "mdl-list__item-icon transaction__icon"}
                                          (or icon "💰")])
                                       [:span {:class "mdl-list__item-primary-content transaction__text"}
                                        [:span {:class "transaction__amount"} (format-amount currency (js/Math.abs amount))]
                                        [:span {:class "transaction__description-lines"}
                                         [:span {:class "transaction__description-primary"}
                                          (or merchant desc)]
                                         (when-let [addr (:short_formatted address)]
                                           [:span {:class "transaction__description-secondary"} addr])]
                                        [:span {:class "transaction__date"}
                                         (format-date-time created)]]]))))]]))))]])))
