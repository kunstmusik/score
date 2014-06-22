(ns score.freq-test
  (:require [clojure.test :refer :all]
            [score.freq :refer :all]))

(deftest keyword->notenum-test
  (testing "keyword->notenum test"
    (is (= 60 (keyword->notenum :C4)))
    (is (= 61 (keyword->notenum :C#4)))
    (is (= 70 (keyword->notenum :Bb4)))
    (is (= 70 (keyword->notenum :A#4)))
    
    ))
