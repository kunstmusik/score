(ns score.mask.generators-test
  (:require [clojure.test :refer :all]
            [score.core :refer :all]
            [score.mask.bpf :refer :all]
            [score.mask.generators :refer :all]
            ))

(deftest add-gen-test
  (testing "add-gen with constants equals 4 for all times"
    (let [gen (add-gen 1 3)]
      (is (= 4 (gen 0.0)))
      (is (= 4 (gen 0.25)))
      (is (= 4 (gen 0.5)))
      (is (= 4 (gen 0.75)))
      ))
   (testing "add-gen with bpf "
    (let [gen (add-gen (bpf [0 0 1 1]) 3)]
      (is (= 3.0 (gen 0.0)))
      (is (= 3.25 (gen 0.25)))
      (is (= 3.5 (gen 0.5)))
      (is (= 3.75 (gen 0.75)))
      )))


(deftest mul-gen-test
  (testing "mul-gen with constants equals 3 for all times"
    (let [gen (mul-gen 1 3)]
      (is (= 3 (gen 0.0)))
      (is (= 3 (gen 0.25)))
      (is (= 3 (gen 0.5)))
      (is (= 3 (gen 0.75)))
      ))
   (testing "mul-gen with bpf "
    (let [gen (mul-gen (bpf [0 0 1 1]) 4)]
      (is (= 0.0 (gen 0.0)))
      (is (= 1.0 (gen 0.25)))
      (is (= 2.0 (gen 0.5)))
      (is (= 3.0 (gen 0.75)))
      )))
