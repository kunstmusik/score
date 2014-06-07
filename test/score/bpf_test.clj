(ns score.bpf-test
  (:require [clojure.test :refer :all]
            [score.bpf :refer :all]))

(def test-pts 
  (partition 2 [0 0 1 0.5 2 0.75 3 0.25 4 1]))

(deftest find-points-test
  (testing "find-points"
    (is (= [[0 0] [1 0.5]] 
           (find-points 0 test-pts)))
    (is (= [[1 0.5] [2 0.75]] 
           (find-points 1 test-pts)))
    (is (= [[4 1] nil] 
           (find-points 5 test-pts)))
    ))

(deftest interp-linear-test
  (testing "interp-linear"
   
   (let [[a b] (find-points 0 test-pts)] 
     (is (= 0.0 (interp-linear 0 a b)))
     (is (= 0.25 (interp-linear 0.5 a b)))
     ) 

   (let [[a b] (find-points 1.0 test-pts)] 
     (is (= 0.5 (interp-linear 1.0 a b)))
     (is (= 0.625 (interp-linear 1.5 a b)))
     ) 
    ))
