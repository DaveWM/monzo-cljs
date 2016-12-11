(ns monzo-cljs.home-page
  (:require [posh.reagent :refer [pull]]))

(defn header [title]
  [:header {:class "mdl-layout__header"}
   [:div {:class "mdl-layout__header-row"}
    [:img {:src "images/monzo-logo.png" :class "header__logo"}]
    [:span {:class "mdl-layout-title"} title]]])

(defn content []
  [:main {:class "mdl-layout__content"}
   [:p (apply str (repeat 25 "Content"))]])

(defn home-page [conn]
  (let [{title :app/title} @(pull conn '[:app/title] 1)]
    [:div {:class "mdl-layout mdl-layout--fixed-header"}
     (header title)
     (content)]))
