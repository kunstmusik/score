(ns score.mask-test
  (:require [clojure.test :refer :all]
            [score.oscillators :refer :all]
            [score.mask :refer :all]))

(deftest mask-test
  (testing "Mask test"
    (let [gen (mask -2 2 (triangle))]
      
      (is (= -2.0 (gen 0.0)))
      (is (= 0.0 (gen 0.25)))
      (is (= 2.0 (gen 0.5)))
      (is (= 0.0 (gen 0.75)))

      )))
