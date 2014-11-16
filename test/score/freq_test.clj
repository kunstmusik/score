(ns score.freq-test
  (:require [clojure.test :refer :all]
            [score.freq :refer :all]))

(deftest midi->freq-test
  (testing "midi->freq"
    (is (= 220.0 (midi->freq 57)))  
    (is (= 440.0 (midi->freq 69)))  
    (is (= 220.0 (midi->freq (keyword->notenum :A3))))
    (is (= 440.0 (midi->freq (keyword->notenum :A4))))
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

(deftest pch-add-test
  (testing "pch-add with 12-TET"
    (is (= [8 5] (pch-add [8 0] 5))) 
    (is (= [9 11] (pch-add [8 0] 23))) 
    (is (= [6 10] (pch-add [8 0] -14))) )
  (testing "pch-add with non-standard octave"
    (is (= [9 0] (pch-add [8 0] 13 13)))
    (is (= [7 12] (pch-add [8 0] -3 15)))))

(deftest pch-diff-test
  (testing "pch-diff with 12-TET"
    (is (= 12 (pch-diff [8 0] [9 0]))) 
    (is (= 15 (pch-diff [8 0] [9 3]))) 
    (is (= -13 (pch-diff [8 0] [6 11]))))
  (testing "pch-diff with non-standard octave"
    (is (= 17 (pch-diff [8 0] [9 4] 13))) 
    (is (= 8 (pch-diff [8 0] [9 3] 5))) 
    (is (= -10 (pch-diff [8 0] [7 43] 53)))))
