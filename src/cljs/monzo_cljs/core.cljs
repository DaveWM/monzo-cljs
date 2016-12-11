(ns monzo-cljs.core
  (:require
   [reagent.core :as reagent]
   [posh.reagent :refer [pull q posh! transact!]]
   [datascript.core :as d]
   [monzo-cljs.home-page :refer [home-page]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce debug?
  ^boolean js/goog.DEBUG)

(defonce app-state
  (reagent/atom
   {:text "Hello, what is your name? "}))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Posh

(def conn (d/create-conn))

(posh! conn)

(transact! conn [[:db/add 1 :app/title "Monzo Web"]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")
    ))

(defn reload []
  (reagent/render [home-page conn]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
