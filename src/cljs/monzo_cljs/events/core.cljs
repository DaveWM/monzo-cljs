(ns monzo-cljs.events.core
  (:require [cljs.core.async :refer [pipe chan put! <!]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defmulti process-event first)
(defmethod process-event :default []
  (println "default")
  nil)

(defn actions->transactions! [event-chan app-db dependencies]
  (let [transaction-chan (chan)]
    (go-loop []
      (let [event (<! event-chan)
            [db-update command] (process-event event @app-db)]
        (println event)
        (when db-update
          (put! transaction-chan [event db-update]))
        (when-let [command-chan (and command (command @app-db dependencies))]
          (pipe command-chan event-chan false))
        (recur)))
    transaction-chan))
