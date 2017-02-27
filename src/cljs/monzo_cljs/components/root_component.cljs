(ns monzo-cljs.components.root-component
  (:require [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.components.home-page :refer [home-page]]
            [monzo-cljs.utilities :refer [format-amount]]
            [datascript.core :refer [pull]]
            [clojure.string :as str]
            [cemerick.url :refer [url]]
            [reagent.core :as r]
            [monzo-cljs.reagent-mdl :refer [Button IconButton Menu MenuItem Layout Content Header HeaderRow]]))

(def route-component-map {:routes/home home-page
                          :routes/oauth #(vec [:span "Authenticating..."])})

(defn header [title balance spent-today currency username]
  [Header {:class "app-bar"}
   [HeaderRow {:class "app-bar__row"}
    [:img {:src "images/monzo-logo.png" :class "app-bar__logo"}]
    [:span {:class "mdl-layout-title app-bar__title"} title]
    (let [url-username (when username (-> username
                                          (str/replace #"\s+" "")
                                          (str/lower-case)))
          payments-url (url "https://monzo.me")
          topup-url (when url-username (url payments-url url-username))]
      [:div
       [IconButton {:class "mdl-button--raised"
                    :name "local_atm"
                    :id "payments-menu"}]
       [Menu {:target "payments-menu"
              :align "right"
              :valign "bottom"
              :class "menu"}
        [MenuItem [:a {:href (str payments-url)} "Send Payment"]]
        [MenuItem [:a {:href (str topup-url)} "Top Up"]]]])]
   [:div {:class "app-bar__extra-info"}
    [:span {:class "app-bar__extra-info-section"}
     [:h4 "Balance"]
     [:h5 (if balance
            (format-amount currency balance)
            "-")]]
    [:span {:class "app-bar__extra-info-section"}
     [:h4 "Spent Today"]
     [:h5 (if spent-today
            (format-amount currency (js/Math.abs spent-today))
            "-")]]]])

(defn root-component [conn event-chan]
  (let [{:keys [app/title app/selected-account] route :routes/current}
        (pull conn '[:app/title :routes/current :app/selected-account] app-datom-id)
        {:keys [balance/currency balance/balance] spent-today :balance/spend_today username :account/description}
        (when selected-account
          (pull conn '[:balance/balance :balance/currency :balance/spend_today :account/description] selected-account))
        page (get route-component-map route)]
    [Layout {:fixedHeader true}
     (header title balance spent-today currency username)
     [Content {:class "layout"
               :component "main"}
      (when page
        (page conn event-chan))]]))


