(ns score.amp-test
  (:require [clojure.test :refer :all]
            [score.amp :refer :all]))

(deftest db->amp-test
  (testing "db->amp"
    (is (= 1.0 (db->amp 0)))  
    (is (= "0.708"  (format "%.3g" (db->amp -3))))  
    ))
