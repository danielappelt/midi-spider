(ns midi-spider.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [midi-spider.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
