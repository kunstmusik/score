(ns score.accumulator-test
  (:require [clojure.test :refer :all]
            [score.core :refer :all]
            [score.accumulator :refer :all]))

(deftest accumulate-test
  (testing "accumulate test"
    (let [gen (accumulate (const 2))]
      
      (is (= 2 (gen 0.0)))
      (is (= 4 (gen 0)))
      (is (= 6 (gen 0)))
      (is (= 8 (gen 0)))

      )

    (let [gen (accumulate (const 2) 3)]
      
      (is (= 5 (gen 0.0)))
      (is (= 7 (gen 0)))
      (is (= 9 (gen 0)))
      (is (= 11 (gen 0)))
      )
    ))

(deftest limit-test
  (testing "limit"
    (is (= 2 (limit -1 2 4))) 
    (is (= 4 (limit 5 2 4))) 
    (is (= 3 (limit 3 2 4))) 
    (is (= 2 (limit 3 2 2))) 
    ))

(deftest accumulate-limit-test
  (testing "accumulate-limit"
    (let [gen (accumulate-limit 3 6 (const 2))]
      
      (is (= 3 (gen 0.0)))
      (is (= 5 (gen 0)))
      (is (= 6 (gen 0)))
      (is (= 6 (gen 0)))

      )
    ))

(deftest mirror-test
  (testing "mirror"
    (is (= 3 (mirror -1 2 4))) 
    (is (= 3 (mirror 5 2 4))) 
    (is (= 3 (mirror 3 2 4))) 
    (is (= 2.5 (mirror 1.5 2 4))) 
    (is (= 2 (mirror 1.5 2 2))) 
    (is (= 3 (mirror 71 2 4))) 
    ))


(deftest accumulate-mirror-test
  (testing "accumulate-mirror"
    (let [gen (accumulate-mirror 3 6 (const 2))]
      (is (= 4 (gen 0.0)))
      (is (= 6 (gen 0)))
      (is (= 4 (gen 0)))
      (is (= 6 (gen 0)))
      )
    ))


(deftest wrap-test
  (testing "wrap"
    (is (= 3.5 (wrap 1.5 2 4))) 
    (is (= 2.5 (wrap 4.5 2 4))) 
    (is (= 3 (wrap 3 2 4))) 
    (is (= 3.5 (wrap 1.5 2 4))) 
    (is (= 2 (wrap 1.5 2 2))) 
    (is (= 2.5 (wrap 8.5 2 4))) 
    ))


(deftest accumulate-wrap-test
  (testing "accumulate-wrap"
    (let [gen (accumulate-wrap 3 6.5 (const 2))]
      (is (= 5.5 (gen 0.0)))
      (is (= 4.0 (gen 0)))
      (is (= 6.0 (gen 0)))
      (is (= 4.5 (gen 0)))
      )
    ))

