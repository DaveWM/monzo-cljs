(ns monzo-cljs.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [monzo-cljs.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
