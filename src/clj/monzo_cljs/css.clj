(ns monzo-cljs.css
  (:require [garden.def :refer [defstyles]]))

(defstyles app
  [:body
   [:header {:position "relative"}
    [:.header__logo {:position "absolute"
                     :left "8px"
                     :height "90%"}]]
   [:main.layout {:justify-content "center"
                  :display "flex"
                  :padding-top "15px"}]
   [:.home-card {:width "100%"
                 :max-width "900px"}
    [:p {:text-align "center"}]]])
