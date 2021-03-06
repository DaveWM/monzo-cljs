(ns monzo-cljs.css
  (:require [garden.def :refer [defstyles]]
            [garden.stylesheet :refer [at-media]]
            [garden.selectors :as s]))

(def light-text-colour "rgba(0,0,0,0.54)")
(def debit-colour "#D50000")
(def credit-colour "#8BC34A")
(def monzo-blue "#14233c")

(def mobile {:max-width "720px"})

(def header-styles {:color "white"
                    :font-family "'Karla', sans-serif"})
(def big-font {:font-size "18px"
               :font-weight "600"})

(def button [:.button {:margin "0 5px"}
             [:&--monzo-blue {:background-color "#C1D0E9"}
              [:&:hover {:background-color "#96A5BE"}]]
             [:&--disabled {:opacity "0.3"
                            :cursor "not-allowed"}]])

(def menu [:.menu {:position "relative"}
           [:.menu__dropdown {:position "absolute"
                              :right "50%"
                              :background "white"
                              :box-shadow "0 2px 2px 0 rgba(0,0,0,.14), 0 3px 1px -2px rgba(0,0,0,.2), 0 1px 5px 0 rgba(0,0,0,.12)"
                              :padding "8px 0"
                              :margin "5px 0"}
            [:.menu__dropdown-item {:opacity "1"}
             [:a {:text-decoration "none"
                  :color "black"}]]]])

(def flex-padder [:.flex-padder {:flex "100"}])

(defstyles app
  [:body {:height "100%"
          :background-color "#F5F5F5"}
   button
   flex-padder
   menu
   [:.app-bar {:position "relative"
               :background-color monzo-blue
               :display "flex !important"}
    [:.app-bar__row {:position "relative"
                     :height "inherit"
                     :margin "10px 0"
                     :padding "0 8px"}]
    [:.app-bar__logo {:height "35px"
                      :margin-right "15px"}]
    [:.app-bar__title (merge header-styles
                             {:font-weight "800"
                              :flex 1
                              :text-align "left"})
     (at-media mobile [:& {:text-align "center"}])]
    [:.app-bar__extra-info {:display "flex"
                            :justify-content "space-around"
                            :align-items "center"}
     [:.app-bar__extra-info-section {:text-align "center"
                                     :flex "1"}
      [:h4 (merge header-styles
                  {:margin-top "0"})]
      [:h5 header-styles]]]]
   [:main.layout {:height "100%"
                  :justify-content "center"
                  :display "flex"
                  :padding-top "15px"}]
   [:.home-card {:width "100%"
                 :max-width "1200px"}
    [:.home-card__header {:background-color monzo-blue
                          :align-items "flex-start"
                          :flex-direction "column"
                          :position "relative"}
     [:.home-card__title {:display "flex"
                          :justify-content "flex-start"
                          :width "100%"
                          :margin "10px 0"}]
     [:.home-card__refresh {:position "absolute"
                            :right "20px"
                            :top "0px"
                            :height "100%"
                            :display "flex"
                            :align-items "center"}
      (at-media mobile [:& {:align-items "flex-start"
                            :padding-top "20px"}])]
     [:.home-card__controls {:display "flex"
                             :flex-direction "row"}]
     [:.home-card__control {:display "flex"
                            :flex-direction "column"
                            :justify-content "center"
                            :align-items "center"
                            :margin-right "20px"}]
     [:.home-card__options {:display "flex"
                            :flex-direction "row"}]
     [:i {:color "white"}]
     (map #(vec [% (merge header-styles {:margin 0})]) [:h2 :h3 :h4 :h5 :p])]
    [:.home-card__refresh-button {:color "white"}]]
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
                          :text-align "center"
                          :margin "0 5px 0 0"}
     (at-media mobile [:& {:width "25px"
                           :font-size "23px"}])]
    [:.transaction__text {:display "flex"}
     [:.transaction__amount (merge big-font {:display "flex"
                                             :flex-basis "80px"
                                             :justify-content "center"
                                             :margin "0 12px"})
      [:&--not-included {:text-decoration "line-through"}]
      (at-media mobile [:& {:font-size "16px"
                            :flex-basis "72px"
                            :margin "0 8px"}])]
     [:.transaction__description-lines {:display "flex"
                                        :flex 4
                                        :flex-direction "column"
                                        :align-content "center"
                                        :justify-content "center"
                                        :margin "0 8px"}
      [:.transaction__description-primary {}]
      [:.transaction__description-secondary {:font-size "14px"
                                             :font-weight "400"
                                             :letter-spacing "0"
                                             :line-height "18px"
                                             :color light-text-colour}
       [:&--warning {:color debit-colour}]
       (at-media mobile [(s/& (s/not :.transaction__description-secondary--warning)) {:display "none"}])]]
     [:.transaction__date {:display "flex"
                           :justify-content "flex-end"
                           :flex 1
                           :flex-grow 0
                           :color light-text-colour
                           :font-size "0.9em"
                           :margin "0 5px"
                           :text-align "center"}]]]
   [:.menu
    [:a {:color "black"}]]])
