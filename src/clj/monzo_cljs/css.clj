(ns monzo-cljs.css
  (:require [garden.def :refer [defstyles]]))

(def light-text-colour "rgba(0,0,0,0.54)")
(def debit-colour "#D50000")
(def credit-colour "#8BC34A")
(def monzo-blue "#14233c")

(defstyles app
  [:body
   [:header {:position "relative"}
    [:.header__logo {:position "absolute"
                     :left "8px"
                     :height "90%"}]]
   [:main.layout {:height "100%"
                  :justify-content "center"
                  :display "flex"
                  :padding-top "15px"
                  :background-color "#F5F5F5"}]
   [:.home-card {:width "100%"
                 :max-width "1200px"}
    [:.home-card__title {:height "100px"
                         :color "white"
                         :background-color monzo-blue}
     [:h2 {:font-family "'Karla', sans-serif"
           :font-weight "800"}]]]
   [:.group
    [:.group__header {:margin-bottom "3px"}]
    [:.group__sub-header {:margin-top "0"
                          :font-size "1em"
                          :color light-text-colour}
     [:&--negative {:color debit-colour}]
     [:&--positive {:color credit-colour}]]]
   [:.transaction {:display "flex"
                   :border-left "2px solid"}
    [:&--credit {:border-color credit-colour}]
    [:&--debit  {:border-color debit-colour}]
    [:.transaction__icon {:width "45px"
                          :font-size "45px"
                          :height "auto"
                          :border-radius "5px"
                          :text-align "center"}]
    [:.transaction__text {:display "flex"}
     [:.transaction__amount {:display "flex"
                             :flex "1"
                             :justify-content "center"
                             :font-size "18px"
                             :font-weight "600"}]
     [:.transaction__description-lines {:display "flex"
                                        :flex "5"
                                        :flex-direction "column"
                                        :align-content "center"
                                        :justify-content "center"}
      [:.transaction__description-primary {}]
      [:.transaction__description-secondary {:font-size "14px"
                                             :font-weight "400"
                                             :letter-spacing "0"
                                             :line-height "18px"
                                             :color light-text-colour}]]
     [:.transaction__date {:display "flex"
                           :flex "2"
                           :justify-content "flex-end"
                           :color light-text-colour
                           :font-size "0.9em"}]]]])
