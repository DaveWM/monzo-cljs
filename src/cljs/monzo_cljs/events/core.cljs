(ns monzo-cljs.events.core
  (:require [cljs.core.async :refer [pipe]]
            [datascript.core :as d])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defmulti process-event first)
(defmethod process-event :default []
  (println "default")
  nil)

(defn start-event-loop [event-chan app-db dependencies]
  (go-loop []
    (let [event (<! event-chan)
          [db-update command] (process-event event @app-db)]
      (println event)
      (when db-update
        (d/transact! app-db db-update))
      (when-let [command-chan (and command (command @app-db dependencies))]
        (pipe command-chan event-chan false))
      (recur))))
