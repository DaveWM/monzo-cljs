(ns monzo-cljs.db
  (:require [posh.reagent :refer [pull q posh! transact!]]
            [datascript.core :as d]))

(def app-db (d/create-conn))
(def app-datom-id 1)

(posh! app-db)

(transact! app-db [[:db/add app-datom-id :app/title "Monzo Web"]])
