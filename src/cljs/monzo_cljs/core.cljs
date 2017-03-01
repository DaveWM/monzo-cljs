(ns monzo-cljs.core
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [chan]]
            [monzo-cljs.components.root-component :refer [root-component]]
            [monzo-cljs.db :refer [get-app-db save-app-db]]
            [monzo-cljs.events.core :refer [start-event-loop]]
            [monzo-cljs.routing :refer [start-router!]]
            [monzo-cljs.events.auth]
            [monzo-cljs.events.home-page]
            [monzo-cljs.events.routes]
            [reagent.core :as reagent]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce debug?
  ^boolean js/goog.DEBUG)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App
(when debug?
  (enable-console-print!)
  (println "dev mode"))

(if (aget js/navigator "serviceWorker")
  (-> js/navigator
      .-serviceWorker
      (.register "./serviceWorker.js" (clj->js {:scope "./"}))
      (.then #(println "service worker registered"))))

(defonce app-db (get-app-db))
(defonce r-app-db (reagent/atom @app-db))

(defonce events-chan (chan))

(def dependencies {:window js/window
                   :http-get http/get
                   :http-post http/post
                   :local-storage js/localStorage})

(defn container [child]
  (let [db @r-app-db]
    [child db events-chan]))

(defn reload []
  (reagent/render [container root-component]
                  (.getElementById js/document "app")))

(add-watch app-db :render #(reset! r-app-db @app-db))
(add-watch app-db :save #(save-app-db @app-db (:local-storage dependencies)))

(defn main []
  (start-event-loop events-chan app-db dependencies)
  (start-router! events-chan)
  (reload))

(main)
