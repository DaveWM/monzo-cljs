(ns monzo-cljs.core
  (:require
   [reagent.core :as reagent]
   [monzo-cljs.home-page :refer [home-page]]
   [monzo-cljs.db :refer [app-db]]))


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

(defn reload []
  (reagent/render [home-page app-db]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
