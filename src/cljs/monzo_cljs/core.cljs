(ns monzo-cljs.core
  (:require
   [reagent.core :as reagent]
   [posh.reagent :refer [pull q posh! transact!]]
   [datascript.core :as d]))


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
(def app-datom-id 1)

(posh! conn)

(transact! conn [[:db/add app-datom-id :app/title "Monzo Web"]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(defn page [conn]
  (let [{title :app/title} @(pull conn '[:app/title] app-datom-id)]
    [:p title]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")
    ))

(defn reload []
  (reagent/render [page conn]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
