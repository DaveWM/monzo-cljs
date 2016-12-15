(ns monzo-cljs.routing
  (:require [domkm.silk :as silk]
            [pushy.core :as pushy]
            [cljs.core.async :refer [put!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def oauth-route ["oauth"])

(def routes
  (silk/routes [[:routes/root [[]]]
                [:routes/oauth [oauth-route {"code" :code}]]]))

(defn get-route-url [route-name]
  (silk/depart routes route-name))

(defn start-router! [events-chan]
  (-> (pushy/pushy (fn [match]
                     (go
                       (put! events-chan [(:domkm.silk/name match) match])))
                   (partial silk/arrive routes))
      (pushy/start!)))
