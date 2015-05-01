(ns score.sieves-test
  (:require [clojure.test :refer :all]
            [score.sieves :refer :all]))

(deftest gen-sieve-test
  (testing "Generation of sequences with sieves"
    (is (= [0 2 4 6] (take 4 (gen-sieve [2 0])))) 
    (is (= [1 2 3 5 7 8 9 11 13 14] 
           (take 10 (gen-sieve (U [2 1] [3 2]))))) 
    (is (= [5 11 17 23 29 35 41 47 53 59] 
           (take 10 (gen-sieve (I [2 1] [3 2]))))) 
    ))

(deftest normalize-test
  (testing "Normalize"
    (is (= [4 1] (normalize [4 5])) ) 
    (is (= [11 0] (normalize [11 22])) ) 

    (is (= (U [3 0] [4 3]) 
           (normalize (U [3 9] [4 7])))) 

    (is (= (I [4 1] (U [3 0] [4 3])) 
           (normalize (I [4 5] (U [3 9] [4 7]))))) 
    ))
