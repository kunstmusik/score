(ns score.freq-test
  (:require [clojure.test :refer :all]
            [score.freq :refer :all]))

(deftest midi->freq-test
  (testing "midi->freq"
    (is (= 440.0 (midi->freq 57)))  
    (is (= 880.0 (midi->freq 69)))  
    (is (= 440.0 (midi->freq (keyword->notenum :A3))))
    (is (= 880.0 (midi->freq (keyword->notenum :A4))))
    ))

(deftest keyword->notenum-test
  (testing "keyword->notenum test"
    (is (= 57 (keyword->notenum :A3)))
    (is (= 60 (keyword->notenum :C4)))
    (is (= 61 (keyword->notenum :C#4)))
    (is (= 70 (keyword->notenum :Bb4)))
    (is (= 70 (keyword->notenum :A#4)))
    ))

(deftest pch-interval-seq-test
  (testing "pch-interval-seq test"
   
    (is (= [[8 0] [8 2] [8 3] [8 1]]
           (pch-interval-seq [8 0] 2 1 -2))) 
    (is (= [[8 0] [8 2] [8 3] [8 1]]
           (pch-interval-seq [8 0] [2 1 -2]))) 
    (is (= [[8 0] [8 2]]
           (pch-interval-seq [8 0] 2)))
    (is (= [[8 0] [8 2]]
           (pch-interval-seq [8 0] [2])))
    (is (= [[8 0]]
           (pch-interval-seq [8 0] [])))
    ))

(deftest invert-test
  (testing "invert test"
    (is (= [[8 0] [8 3] [7 7]]
           (invert (pch-interval-seq [8 0] 3 4 ) 1 ))) 
    (is (= [[8 0] [7 3] [7 7]]
           (invert (pch-interval-seq [8 0] 3 4 ) 2 ))) 
    (is (= [[8 0] [7 3] [6 7]]
           (invert (pch-interval-seq [8 0] 3 16 ) 2 ))) 
    ))

