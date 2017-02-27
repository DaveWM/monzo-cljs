(ns monzo-cljs.home-page
  (:require [datascript.core :refer [pull q]]
            [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.utilities :refer [format-amount]]
            [cljs-time.core :as time :refer [year month day months weeks minus]]
            [cljs-time.coerce :refer [from-string]]
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

(defn declined? [{reason :transaction/decline_reason}]
  (boolean reason))

(defn sum-transactions [transactions]
  (reduce (fn [sum {:keys [transaction/amount] :as t}]
            (+ sum (when (not (declined? t)) amount)))
          0
          transactions))

(defn snake-case-to-capitalised [s]
  (when s
    (->> (split s #"_")
         (map capitalize)
         (join " "))))

(defn get-transactions-currency [transactions]
  "returns the common currency for a list of transactions, or nil if the transactions are in different currencies"
  (let [currencies (->> transactions
                        (map :transaction/currency)
                        (into #{}))]
    (when (= 1 (count currencies))
      (first currencies))))


(def grouping-functions
  {:date {:grouping #(-> %
                         :transaction/created
                         goog.date.Date.)
          :header format-date
          :sort-value #(- (.valueOf %))
          :transaction-date-format time-only-format} 
   :merchant {:grouping #(get-in % [:transaction/merchant-id :merchant/name])
              :transaction-date-format date-time-format}
   :category {:grouping #(->> (get-in % [:transaction/merchant-id :merchant/category])
                              snake-case-to-capitalised)
              :transaction-date-format date-time-format}})

(def sorting-functions
  {:default key
   :total-spend #(->> (val %)
                      (remove declined?)
                      (map :transaction/amount)
                      (reduce + 0))
   :count #(->> (val %)
                count
                -)})

(defn transactions-control [{:keys [label options active? on-option-clicked]}]
  [:div.home-card__control
     [:p label]
     [:div.home-card__options
      (map
       (fn [[option icon]]
         ^{:key option}
         [IconButton {:name icon
                      :class (when (active? option) "mdl-button--raised")
                      :on-click (partial on-option-clicked option)}])
       options)]])

(defn transactions-card-header [event-chan {:keys [transactions/loading transactions/selected-group transactions/selected-sort transactions/sort-direction transactions/selected-limit]}]
  [CardTitle {:class "home-card__header"}
   [:div.flex-padder]
   [:div.home-card__title
    [:h2 {:class "mdl-card__title-text"} "Transactions"]
    [:span {:class "home-card__refresh"}
     (if loading
       [Spinner]
       [IconButton {:class "home-card__refresh-button"
                    :name "refresh"
                    :on-click #(go (put! event-chan [:action/refresh-transactions]))}])]]
   [:div.home-card__controls
    [transactions-control {:label "Group"
                           :options {:date "event_note"
                                     :merchant "local_convenience_store"
                                     :category "format_list_numbered"}
                           :active? (partial = selected-group)
                           :on-option-clicked #(go (put! event-chan [:action/select-transaction-grouping %]))}]
    [transactions-control {:label "Sort"
                           :options {:default "keyboard_arrow_down"
                                     :total-spend "local_atm"
                                     :count "shop_two"}
                           :active? (partial = selected-sort)
                           :on-option-clicked #(go (put! event-chan (if (= % selected-sort)
                                                                      [:action/change-transaction-sort-direction]
                                                                      [:action/select-transaction-sorting %])))}]
    [transactions-control {:label "Limit"
                           :options {:week "view_week"
                                     :month "event_note"
                                     :all "all_inclusive"}
                           :active? (partial = selected-limit)
                           :on-option-clicked #(go (put! event-chan [:action/select-transaction-limit %]))}]]])

(defn transactions-card-body [{:keys [transactions/selected-group transactions/selected-sort transactions/sort-direction transactions/selected-limit] :as app-data} data]
  (let [current-date (-> app-data
                         :app/current-date
                         from-string)
        {:keys [grouping ordering sort-value transaction-date-format header]
         :or {header str
              sort-value identity}}
        (selected-group grouping-functions)
        sort-fn (selected-sort sorting-functions)
        limit-fn (condp = selected-limit
                   :all (constantly true)
                   :week (fn [{created :transaction/created}] (< (minus current-date (weeks 1)) created))
                   :month (fn [{created :transaction/created}] (< (minus current-date (months 1)) created)))]
    [:div {:class "mdl-card__supporting-text"}
     (->> data
          (filter limit-fn)
          (group-by grouping)
          (filter key)
          (sort-by (fn [[group data]]
                     (-> [(sort-value group) data]
                         sort-fn))
                   (if sort-direction < >))
          (map (fn [[group-key transactions-data]]
                 ^{:key group-key}
                 [:div {:class "group"}
                  [:h4 {:class "group__header"} (header group-key)]
                  (let [sum (sum-transactions transactions-data)
                        currency (get-transactions-currency transactions-data)]
                    (when currency
                      [:h5 {:class (str "group__sub-header "
                                        (if (pos? sum)
                                          "group__sub-header--positive"
                                          "group__sub-header--negative"))}
                       (str " " (format-amount currency sum))]))
                  [List
                   (->> transactions-data
                        (sort-by :transaction/created >)
                        (map (fn [{:keys [db/id transaction/created transaction/amount transaction/description transaction/currency transaction/notes] decline-reason :transaction/decline_reason included? :transaction/included_in_spending merchant :transaction/merchant-id :as transaction}]
                               (let [is-credit (pos? amount)
                                     declined (declined? transaction)]
                                 ^{:key id}
                                 [ListItem {:class (str "transaction "
                                                        (if is-credit "transaction--credit" "transaction--debit"))}
                                  (let [{logo :merchant/logo icon :merchant/emoji} merchant]
                                    (if (not (blank? logo))
                                      [:img {:class "mdl-list__item-icon transaction__icon"
                                             :src logo}]
                                      [:span {:class "mdl-list__item-icon transaction__icon"}
                                       (or icon "💰")]))
                                  [:span {:class "mdl-list__item-primary-content transaction__text"}
                                   [:span {:class (str "transaction__amount "
                                                       (when declined
                                                         "transaction__amount--not-included"))}
                                    (format-amount currency (js/Math.abs amount))]
                                   [:span {:class "transaction__description-lines"}
                                    [:span {:class "transaction__description-primary"}
                                     (or (:merchant/name merchant) description)]
                                    [:span {:class  (str "transaction__description-secondary "
                                                         (when declined
                                                           "transaction__description-secondary--warning"))}
                                     (or (get decline-reasons decline-reason)
                                         notes
                                         (get-in merchant [:merchant/address :short_formatted]))]]
                                   [:span {:class "transaction__date"}
                                    (time-format/unparse transaction-date-format (goog.date.DateTime. created))]]]))))]])))]))

(defn transactions-card [event-chan app-data transactions-data]
  [Card {:class "home-card"}
   [transactions-card-header event-chan app-data]
   [transactions-card-body app-data transactions-data]])

(defn home-page [app-db event-chan]
  (let [transaction-data (q '[:find [(pull ?e [:db/id :transaction/description :transaction/amount :transaction/currency :transaction/created :transaction/metadata :transaction/decline_reason :transaction/include_in_spending :transaction/merchant-id {:transaction/merchant-id [:merchant/emoji :merchant/logo :merchant/name :merchant/address :merchant/category]}]) ...]
                    :in $
                    :where [?e :transaction/id]]
                  app-db)
        app-data (d/pull app-db ["*"] app-datom-id)]
    (if (empty? transaction-data)
      
      [Spinner]
      
      [transactions-card event-chan app-data transaction-data])))
