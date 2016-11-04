(ns score.euclid-test
  (:require [clojure.test :refer :all]
            [score.euclid :refer :all]))


(deftest euclid-test
  (is (= [1 0] (euclid 1 2)))
  (is (= [1 0 0] (euclid 1 3)))
  (is (= [1 0 0 0] (euclid 1 4)))
  (is (= [1 0 0 0 0] (euclid 1 5)))
  (is (= [1 0 0 0 1 0 0 0] (euclid 2 8)))


  ;; patterns from the paper
  (is (= [1 0 1 0 0] (euclid 2 5)))
  (is (= [1 0 0 1 0 0 0] (euclid 2 7)))
  (is (= [1 1 1 0] (euclid 3 4)))
  (is (= [1 0 1 0 1] (euclid 3 5)))
  (is (= [1 0 1 0 1 0 0] (euclid 3 7)))
  (is (= [1 0 0 1 0 0 1 0] (euclid 3 8)))
  (is (= [1 0 0 1 0 0 1 0 0 0] (euclid 3 10)))
  (is (= [1 0 0 0 1 0 0 0 1 0 0] (euclid 3 11)))
  )
