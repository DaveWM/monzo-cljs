(ns monzo-cljs.home-page
  (:require [datascript.core :refer [pull q]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.utilities :refer [format-amount]]
            [cljs-time.core :as time :refer [year month day]]
            [cljs-time.format :as time-format]
            [clojure.string :refer [blank?]]
            [datascript.core :as d])
  (:import [goog.date.DateTime]))

(def date-format (time-format/formatter "dd MMMM yyyy"))
(def date-time-format (time-format/formatter "dd/MM/yyyy HH:mm"))
(def time-only-format (time-format/formatter "HH:mm"))
(def decline-reasons {"INSUFFICIENT_FUNDS" "Transaction declined due to insufficient funds"})

(defn format-date [date]
  (time-format/unparse date-format date))

(defn format-date-time [date]
  (time-format/unparse date-time-format date))

(defn format-time [date]
  (time-format/unparse time-only-format date))

(defn sum-transactions [transactions]
  (->> transactions
       (reduce (fn [sum [_ _ amount _ _ _ _ included?]]
                 (+ sum (when included? amount)))
               0)))

(defn get-transactions-currency [transactions]
  "returns the common currency for a list of transactions, or nil if the transactions are in different currencies"
  (let [currencies (->> transactions
                        (map #(nth % 4))
                        (into #{}))]
    (when (= 1 (count currencies))
      (first currencies))))

(defn home-page [app-db]
  (let [data (->> (q '[:find ?e ?created ?amount ?desc ?currency ?metadata ?decline-reason ?include ?m
                       :in $
                       :where
                       [?e :transaction/description ?desc]
                       [?e :transaction/amount ?amount]
                       [?e :transaction/currency ?currency]
                       [?e :transaction/created ?created]
                       [?e :transaction/metadata ?metadata]
                       [(get-else $ ?e :transaction/decline_reason false) ?decline-reason]
                       [?e :transaction/include_in_spending ?include]
                       [(get-else $ ?e :transaction/merchant-id false) ?m]]
                     app-db)
                  (map (fn [transaction]
                         (let [m-id (last transaction)]
                           {:transaction transaction
                            :merchant (when m-id
                                        (-> (d/pull app-db '[:merchant/emoji :merchant/logo :merchant/name :merchant/address]
                                                   m-id)
                                            ((juxt :merchant/emoji :merchant/logo :merchant/name :merchant/address))))}))))
        {loading :transactions/loading} (pull app-db '[:transactions/loading] app-datom-id)]
    (if (empty? data)
      
      [:div {:class "mdl-spinner mdl-js-spinner is-active"}]
      
      [:div {:class "home-card mdl-card mdl-shadow--2dp"}
       [:div {:class "home-card__title mdl-card__title"}
        [:h2 {:class "mdl-card__title-text"} "Transactions"]
        [:span loading]
        (when loading
          [:div {:class "mdl-spinner mdl-js-spinner is-active"}])]
       [:div {:class "mdl-card__supporting-text"}
        (->> data
             (group-by (comp (juxt year month day)
                             #(goog.date.DateTime. %)
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
                             (map (fn [{[id created amount desc currency {notes :notes} decline-reason included?] :transaction
                                        [icon logo merchant address] :merchant}]
                                    (let [is-credit (pos? amount)
                                          declined? (boolean decline-reason)]
                                      ^{:key id}
                                      [:li {:class (str "transaction mdl-list__item "
                                                        (if is-credit "transaction--credit" "transaction--debit"))}
                                       (if (not (blank? logo))
                                         [:img {:class "mdl-list__item-icon transaction__icon"
                                                :src logo}]
                                         [:span {:class "mdl-list__item-icon transaction__icon"}
                                          (or icon "💰")])
                                       [:span {:class "mdl-list__item-primary-content transaction__text"}
                                        [:span {:class (str "transaction__amount "
                                                            (when declined?
                                                              "transaction__amount--not-included"))}
                                         (format-amount currency (js/Math.abs amount))]
                                        [:span {:class "transaction__description-lines"}
                                         [:span {:class "transaction__description-primary"}
                                          (or merchant desc)]
                                         (let [addr (:short_formatted address)]
                                           [:span {:class  (str "transaction__description-secondary "
                                                             (when declined?
                                                               "transaction__description-secondary--warning"))}
                                            (or (get decline-reasons decline-reason)
                                                notes
                                                addr)])]
                                        [:span {:class "transaction__date"}
                                         (format-time (goog.date.DateTime. created))]]]))))]]))))]])))
