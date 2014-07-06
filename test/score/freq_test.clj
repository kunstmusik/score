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

(deftest pch-interval-seq-test
  (testing "pch-interval-seq test"
   
    (is (= [[8 0] [8 2] [8 3] [8 1]]
           (pch-interval-seq [8 0] 2 1 -2))) 
    ))
