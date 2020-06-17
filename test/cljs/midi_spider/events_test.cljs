(ns midi-spider.events-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [midi-spider.events :as events]))

(deftest strip-to-hex-test
  (testing "Strip to hex: various inputs"
    (is (= (events/strip-to-hex "t ") ""))
    (is (= (events/strip-to-hex "ffff") "ffff"))
    (is (= (events/strip-to-hex "t ff eet tdd tcc aat") "ff ee dd cc aa"))))

(deftest text->hex-array-test
  (testing "Text to hex-array: various inputs"
    (is (= (events/text->hex-array "t ") []))
    (is (= (events/text->hex-array "ffff") ["ffff"]))
    (is (= (events/text->hex-array "t ff eet tdd tcc aat") ["ff" "ee" "dd" "cc" "aa"]))))
