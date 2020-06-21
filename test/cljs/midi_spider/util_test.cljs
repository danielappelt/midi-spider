(ns midi-spider.util-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [midi-spider.util :as util]))

(deftest strip-to-hex-test
  (testing "Strip to hex: various inputs"
    (is (= (util/strip-to-hex "t ") ""))
    (is (= (util/strip-to-hex "ffff") "ffff"))
    (is (= (util/strip-to-hex "t ff eet tdd tcc aat") "ff ee dd cc aa"))))

(deftest text->hex-array-test
  (testing "Text to hex-array: various inputs"
    (is (= (util/text->hex-array "t ") []))
    (is (= (util/text->hex-array "ffff") ["ffff"]))
    (is (= (util/text->hex-array "t ff eet tdd tcc aat") ["ff" "ee" "dd" "cc" "aa"]))))
