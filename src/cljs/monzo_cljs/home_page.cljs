(ns monzo-cljs.home-page
  (:require [datascript.core :refer [pull q]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.utilities :refer [format-amount]]
            [cljs-time.core :as time :refer [year month day]]
            [cljs-time.format :as time-format]
            [cljs-time.extend]
            [clojure.string :refer [blank? capitalize split join]]
            [datascript.core :as d]
            [cljs.core.async :refer [put!]]
            [monzo-cljs.reagent-mdl :refer [Spinner IconButton Card CardTitle List ListItem Icon]]
            [reagent.core :as r])
  (:require-macros [cljs.core.async.macros :refer [go]])
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

(defn declined? [[_ _ _ _ _ _ decline-reason]]
  (boolean decline-reason))

(defn sum-transactions [transactions]
  (->> transactions
       (reduce (fn [sum [_ _ amount :as t]]
                 (+ sum (when (not (declined? t)) amount)))
               0)))

(defn snake-case-to-capitalised [s]
  (when s
    (->> (split s #"_")
         (map capitalize)
         (join " "))))

(defn get-transactions-currency [transactions]
  "returns the common currency for a list of transactions, or nil if the transactions are in different currencies"
  (let [currencies (->> transactions
                        (map #(nth % 4))
                        (into #{}))]
    (when (= 1 (count currencies))
      (first currencies))))


(def grouping-functions
  {:date {:grouping #(-> %
                         :transaction
                         second
                         goog.date.Date.)
          :header format-date
          :sort-value #(- (.valueOf %))
          :transaction-date-format time-only-format} 
   :merchant {:grouping (comp #(nth % 2) :merchant)
              :transaction-date-format date-time-format}
   :category {:grouping (comp snake-case-to-capitalised last :merchant)
              :transaction-date-format date-time-format}})

(def sorting-functions
  {:default key
   :total-spend #(->> (val %)
                      (map :transaction)
                      (remove declined?)
                      (map (fn [[_ _ amount]] amount))
                      (reduce + 0))
   :count #(->> (val %)
                (map :transaction)
                count
                -)})

(defn transactions-card-header [event-chan loading? selected-group selected-sort sort-direction]
  [CardTitle {:class "home-card__header"}
   [:div.flex-padder]
   [:div.home-card__title
    [:h2 {:class "mdl-card__title-text"} "Transactions"]
    [:span {:class "home-card__refresh"}
     (if loading?
       [Spinner]
       [IconButton {:class "home-card__refresh-button"
                    :name "refresh"
                    :on-click #(go (put! event-chan [:action/refresh-transactions]))}])]]
   [:div.home-card__controls
    [:div.home-card__control
     [:p "Group By"]
     [:div.home-card__options
      (map
       (fn [[group icon]]
         ^{:key group}
         [IconButton {:name icon
                      :class (when (= group selected-group) "mdl-button--raised")
                      :on-click #(go (put! event-chan [:action/select-transaction-grouping group]))}])
       {:date "event_note"
        :merchant "local_convenience_store"
        :category "format_list_numbered"})]]
    [:div.home-card__control
     [:p "Sort By"]
     [:div.home-card__options
      (map
       (fn [[sort icon]]
         ^{:key sort}
         [IconButton {:name icon
                      :class (when (= sort selected-sort) "mdl-button--raised")
                      :on-click #(go (put! event-chan (if (= sort selected-sort)
                                                        [:action/change-transaction-sort-direction]
                                                        [:action/select-transaction-sorting sort])))}])
       {:default "keyboard_arrow_down"
        :total-spend "local_atm"
        :count "shop_two"})]]]])

(defn transactions-card-body [selected-group selected-sort sort-direction data]
  (let [{:keys [grouping ordering sort-value transaction-date-format header]
         :or {header str
              sort-value identity}}
        (selected-group grouping-functions)
        sort-fn (selected-sort sorting-functions)]
    [:div {:class "mdl-card__supporting-text"}
     (->> data
          (group-by grouping)
          (filter key)
          (sort-by (fn [[group data]]
                     (-> [(sort-value group) data]
                         sort-fn))
                   (if sort-direction < >))
          (map (fn [[group-key group-data]]
                 (let [group-transactions (map :transaction group-data)]
                   ^{:key group-key}
                   [:div {:class "group"}
                    [:h4 {:class "group__header"} (header group-key)]
                    (let [sum (sum-transactions group-transactions)
                          currency (get-transactions-currency group-transactions)]
                      (when currency
                        [:h5 {:class (str "group__sub-header "
                                          (if (pos? sum)
                                            "group__sub-header--positive"
                                            "group__sub-header--negative"))}
                         (str " " (format-amount currency sum))]))
                    [List
                     (->> group-data
                          (sort-by (comp second :transaction) >)
                          (map (fn [{[id created amount desc currency {notes :notes} decline-reason included? :as transaction] :transaction
                                     [icon logo merchant address] :merchant}]
                                 (let [is-credit (pos? amount)
                                       declined (declined? transaction)]
                                   ^{:key id}
                                   [ListItem {:class (str "transaction "
                                                          (if is-credit "transaction--credit" "transaction--debit"))}
                                    (if (not (blank? logo))
                                      [:img {:class "mdl-list__item-icon transaction__icon"
                                             :src logo}]
                                      [:span {:class "mdl-list__item-icon transaction__icon"}
                                       (or icon "💰")])
                                    [:span {:class "mdl-list__item-primary-content transaction__text"}
                                     [:span {:class (str "transaction__amount "
                                                         (when declined
                                                           "transaction__amount--not-included"))}
                                      (format-amount currency (js/Math.abs amount))]
                                     [:span {:class "transaction__description-lines"}
                                      [:span {:class "transaction__description-primary"}
                                       (or merchant desc)]
                                      (let [addr (:short_formatted address)]
                                        [:span {:class  (str "transaction__description-secondary "
                                                             (when declined
                                                               "transaction__description-secondary--warning"))}
                                         (or (get decline-reasons decline-reason)
                                             notes
                                             addr)])]
                                     [:span {:class "transaction__date"}
                                      (time-format/unparse transaction-date-format (goog.date.DateTime. created))]]]))))]]))))]))

(defn transactions-card [event-chan loading? selected-group selected-sort sort-direction data]
  [Card {:class "home-card"}
   [transactions-card-header event-chan loading? selected-group selected-sort sort-direction]
   [transactions-card-body selected-group selected-sort sort-direction data]])

(defn home-page [app-db event-chan]
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
                                        (-> (d/pull app-db '[:merchant/emoji :merchant/logo :merchant/name :merchant/address :merchant/category]
                                                   m-id)
                                            ((juxt :merchant/emoji :merchant/logo :merchant/name :merchant/address :merchant/category))))}))))
        {:keys [transactions/loading transactions/selected-group transactions/selected-sort :transactions/sort-direction]}
        (pull app-db '[:transactions/loading :transactions/selected-group :transactions/selected-sort :transactions/sort-direction] app-datom-id)]
    (if (empty? data)
      
      [Spinner]
      
      [transactions-card event-chan loading selected-group selected-sort sort-direction data])))
