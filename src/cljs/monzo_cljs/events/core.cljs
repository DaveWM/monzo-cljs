(ns monzo-cljs.events.core
  (:require [cljs.core.async :refer [pipe chan put! <!]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defmulti process-event first)
(defmethod process-event :default []
  (println "default")
  nil)
