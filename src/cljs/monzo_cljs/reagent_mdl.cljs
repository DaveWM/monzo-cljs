(ns monzo-cljs.reagent-mdl
  (:require [reagent.core]
            [cljsjs.react-mdl])
  (:require-macros [monzo-cljs.macros :refer [def-mdl-components]]))

(def-mdl-components [Spinner
                     Button
                     IconButton
                     Card
                     CardTitle
                     Icon
                     List
                     ListItem
                     Menu
                     MenuItem
                     Content
                     Header
                     HeaderRow
                     Layout])
