(ns monzo-cljs.reagent-mdl
  (:require [reagent.core :as r]
            [cljsjs.react-mdl]))

(def Spinner (r/adapt-react-class (.-Spinner js/ReactMDL)))
(def Button (r/adapt-react-class (.-Button js/ReactMDL)))
(def IconButton (r/adapt-react-class (.-IconButton js/ReactMDL)))
(def Card (r/adapt-react-class (.-Card js/ReactMDL)))
(def CardTitle (r/adapt-react-class (.-CardTitle js/ReactMDL)))
(def mdl-List (r/adapt-react-class (aget js/ReactMDL "List")))
(def ListItem (r/adapt-react-class (.-ListItem js/ReactMDL)))
(def Menu (r/adapt-react-class (.-Menu js/ReactMDL)))
(def MenuItem (r/adapt-react-class (aget js/ReactMDL "MenuItem")))
(def Content (r/adapt-react-class (.-Content js/ReactMDL)))
(def Header (r/adapt-react-class (.-Header js/ReactMDL)))
(def HeaderRow (r/adapt-react-class (.-HeaderRow js/ReactMDL)))
(def Layout (r/adapt-react-class (.-Layout js/ReactMDL)))
