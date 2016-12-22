(ns monzo-cljs.root-component
  (:require [monzo-cljs.db :refer [app-datom-id]]
            [monzo-cljs.home-page :refer [home-page]]
            [posh.reagent :refer [pull]]))

(def route-component-map {:routes/home home-page
                          :routes/oauth #(vec [:span "Authenticating..."])})

(defn header [title]
  [:header {:class "mdl-layout__header"}
   [:div {:class "mdl-layout__header-row"}
    [:img {:src "images/monzo-logo.png" :class "header__logo"}]
    [:span {:class "mdl-layout-title"} title]]])

(defn root-component [conn]
  (let [{title :app/title route :routes/current}
        @(pull conn '[:app/title :routes/current] app-datom-id)
        page (get route-component-map route)]
    [:div {:class "mdl-layout mdl-layout--fixed-header"}
     (header title)
     [:main {:class "layout mdl-layout__content"}
      (when page
        (page conn))]]))


