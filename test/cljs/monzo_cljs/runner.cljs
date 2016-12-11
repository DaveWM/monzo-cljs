(ns monzo-cljs.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [monzo-cljs.core-test]))

(doo-tests 'monzo-cljs.core-test)
