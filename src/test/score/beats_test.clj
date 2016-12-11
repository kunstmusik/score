(ns score.beats-test
  (:require [clojure.test :refer :all]
            [score.beats :refer :all]))

(deftest str->pat-test
  (testing "str->pat"
    (is (= [0 0 0 1] (str->pat "0001"))) 
    (is (= [1 0 0 1] (str->pat "1001"))) 
    (is (= [1 0 0 1 0 0 0 0] (str->pat "10010000"))) 
    ))

(deftest pat->set-test 
  (testing "pat->set"
    (is (= #{0 4 6} (pat->set [1 0 0 0 1 0 1 0])))
    (is (= #{1 2 4} (pat->set [0 1 1 0 1 0])))
    (is (= #{0 2 5 7} (pat->set '(1 0 1 0 0 1 0 1))))
    (is (= #{5 7} (pat->set '(0 0 0 0 0 1 0 1))))
    ))

(deftest str->set-test
  (testing "str->set"
    (is (= #{0 2 3} (str->set "1011")))
    (is (= #{4 6 7} (str->set "00001011")))
    (is (= #{0 3 4} (str->set "10011000")))
    ))

(deftest hexchar->str-test
  (testing "hexchar->str"
    (is (= "0000" (hexchar->str \0)))
    (is (= "0001" (hexchar->str \1)))
    (is (= "0010" (hexchar->str \2)))
    (is (= "0100" (hexchar->str \4)))
    (is (= "1000" (hexchar->str \8)))
    (is (= "1010" (hexchar->str \a)))
    (is (= "1111" (hexchar->str \f)))
    ))

(deftest hex->str-test
  (testing "hex->str"
    (is (= "0000" (hex->str "0"))) 
    (is (= "0001" (hex->str "1"))) 
    (is (= "0010" (hex->str "2"))) 
    (is (= "0100" (hex->str "4"))) 
    (is (= "1000" (hex->str "8"))) 
    (is (= "11111010" (hex->str "fa"))) 
    (is (= "1111101010101111" (hex->str "faaf"))) 
    ))


(deftest hex->pat-test
  (testing "hex->pat"
    (is (= [0 1 0 1] (hex->pat "5")))
    (is (= [0 1 1 1 1 1 1 0] (hex->pat "7e")))
    (is (= [0 0 0 0 1 1 1 0] (hex->pat "0e")))
    ))

(deftest hex->set-test 
  (testing "hex->set"
    (is (= #{0 4 6} (hex->set "8a")))
    (is (= #{0 4 6} (hex->set "8a0")))
    (is (= #{0 4 6} (hex->set "8a00")))
    (is (= #{1 2 4} (hex->set "68")))
    (is (= #{1 2 4} (hex->set "680")))
    (is (= #{1 2 4} (hex->set "680")))
    (is (= #{0 2 5 7} (hex->set "a5")))
    (is (= #{5 7} (hex->set "05")))
    ))

