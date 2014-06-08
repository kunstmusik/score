(ns score.quantize-test
  (:require [clojure.test :refer :all]
            [score.oscillators :refer :all]
            [score.mask :refer :all]
            [score.quantize :refer :all]))

(deftest quantize-test
  (testing "quantize test"
    (let [gen (quantize 1 1 0.25 (mask -2 2 (triangle)))]
      
      (is (= -1.75 (gen 0.0)))
      (is (= 0.25 (gen 0.25)))
      (is (= 2.25 (gen 0.5)))
      (is (= 0.25 (gen 0.75)))

      )))
