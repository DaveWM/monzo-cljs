(ns monzo-cljs.css
  (:require [garden.def :refer [defstyles]]))

(defstyles app
  [:header {:position "relative"}
   [:.header__logo {:position "absolute"
                    :left "8px"
                    :height "90%"}]])
