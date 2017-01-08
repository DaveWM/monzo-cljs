(ns monzo-cljs.core
  (:require
   [reagent.core :as reagent]
   [monzo-cljs.root-component :refer [root-component]]
   [monzo-cljs.db :refer [get-app-db save-app-db app-datom-id]]
   [cemerick.url :refer [url]]
   [monzo-cljs.routing :refer [start-router!]]
   [monzo-cljs.events :refer [start-event-loop]]
   [cljs.core.async :refer [chan put!]]
   [datascript.core :as d]
   [cljs-http.client :as http])
  (:require-macros [cljs.core.async.macros :refer [go]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce debug?
  ^boolean js/goog.DEBUG)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")
    ))

(if (aget js/navigator "serviceWorker")
  (-> js/navigator
      .-serviceWorker
      (.register "./serviceWorker.js" (clj->js {:scope "./"}))
      (.then #(println "service worker registered"))))

(defonce app-db (get-app-db))

(defonce events-chan (chan))

(def dependencies {:window js/window
                   :http-get http/get
                   :http-post http/post
                   :local-storage js/localStorage})

(defn reload []
  (reagent/render [root-component @app-db]
                  (.getElementById js/document "app")))

(add-watch app-db :render reload)
(add-watch app-db :save #(save-app-db @app-db))

(defn main []
  (dev-setup)
  (start-event-loop events-chan app-db dependencies)
  (start-router! events-chan)
  (reload))

(main)
