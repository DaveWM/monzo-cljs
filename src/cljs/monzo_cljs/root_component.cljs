(ns monzo-cljs.root-component
  (:require [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.home-page :refer [home-page]]
            [monzo-cljs.utilities :refer [format-amount]]
            [datascript.core :refer [pull]]
            [clojure.string :as str]
            [cemerick.url :refer [url]]))

(def route-component-map {:routes/home home-page
                          :routes/oauth #(vec [:span "Authenticating..."])})

(defn header [title balance spent-today currency username]
  [:header {:class "mdl-layout__header app-bar"}
   [:div {:class "mdl-layout__header-row app-bar__row"}
    [:img {:src "images/monzo-logo.png" :class "app-bar__logo"}]
    [:span {:class "mdl-layout-title app-bar__title"} title]
    [:span.flex-padder]
    (let [url-username (when username (-> username
                                          (str/replace #"\s+" "")
                                          (str/lower-case)))
          payments-url (url "https://monzo.me")
          topup-url (when url-username (url payments-url url-username))]
      [:div.app-bar__buttons
       [:a {:href (str topup-url) :class (str "button button--monzo-blue mdl-button mdl-button--raised "
                                              (when-not topup-url "button--disabled"))}
        "Top up"]
       [:a {:href (str payments-url) :class "button button--monzo-blue mdl-button mdl-button--raised "}
        "Send Money"]])]
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

(defn root-component [conn]
  (let [{title :app/title route :routes/current selected-account :app/selected-account}
        (pull conn '[:app/title :routes/current :app/selected-account] app-datom-id)
        {balance :balance/balance currency :balance/currency spent-today :balance/spend_today username :account/description}
        (when selected-account
          (pull conn '[:balance/balance :balance/currency :balance/spend_today :account/description] selected-account))
        page (get route-component-map route)]
    [:div {:class "mdl-layout mdl-layout--fixed-header"}
     (header title balance spent-today currency username)
     [:main {:class "layout mdl-layout__content"}
      (when page
        (page conn))]]))


