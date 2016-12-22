(defproject monzo-cljs "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [reagent "0.6.0"]
                 [garden "1.3.2"]
                 [ns-tracker "0.3.0"]
                 [posh "0.5.5"]
                 [com.cemerick/url "0.1.1"]
                 [cljs-http "0.1.42"]
                 [com.domkm/silk "0.1.2"]
                 [kibu/pushy "0.3.6"]
                 [com.andrewmcveigh/cljs-time "0.5.0-alpha2"]] 

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "dev"]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-garden "0.2.8"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"
                                    "test/js"
                                    "resources/public/css"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :garden {:builds [{:id           "app"
                     :source-paths ["src/clj"]
                     :stylesheet   monzo-cljs.css/app
                     :compiler     {:output-to "resources/public/css/app.css"
                                    :pretty-print? true}}]}

  
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:dependencies [
                   [figwheel-sidecar "0.5.8"]
                   [com.cemerick/piggieback "0.2.1"]
                   [org.clojure/tools.namespace "0.2.11"]]

    :plugins      [[lein-figwheel "0.5.8"]
                   [lein-doo "0.1.7"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["dev" "src/cljs"]
     :figwheel     {:on-jsload "monzo-cljs.core/reload"}
     :compiler     {:main                 monzo-cljs.core
                    :optimizations        :none
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/dev"
                    :asset-path           "js/compiled/dev"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            monzo-cljs.core
                    :optimizations   :advanced
                    :output-to       "resources/public/js/compiled/app.js"
                    :output-dir      "resources/public/js/compiled/min"
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test"
                    :main          monzo-cljs.runner
                    :optimizations :none}}
    ]})
