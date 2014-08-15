(ns score.tuning-test
  (:require [clojure.test :refer :all]
            [score.tuning :refer :all]))

(defmacro with-private-fns  [[ns fns] & tests]
    "Refers private fns from ns and runs tests in context."
      `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2))  [] fns)
              ~@tests))


(deftest remove-comments-test
  (with-private-fns [score.tuning [remove-comments]]
    (testing "remove-comments"
      (is (= "4/5" (remove-comments "    4/5    "))) 
      (is (= "4/5" (remove-comments "    4/5 cents   "))) 
      (is (= "4/5" (remove-comments "    4/5 34 3434 "))) 
      )))

(deftest convert-cents-test 
  (with-private-fns [score.tuning [convert-cents]]
    (testing "convert-cents"
      (is (= (Math/pow 2 (/ 700.0 1200.0)) (convert-cents "700.0"))) 
      (is (= 2.0 (convert-cents "1200.0"))) 
      (is (= (Math/pow 2 (/ 500.0 1200.0)) (convert-cents "500.0"))) 
      )))

(deftest convert-ratio-test 
  (with-private-fns [score.tuning [convert-ratio]]
    (testing "convert-ratio"
      (is (= 5 (convert-ratio "5"))) 
      (is (= (/ 5 12) (convert-ratio "5/12"))) 
      (is (= 2 (convert-ratio "2/1"))) 
      )))

(deftest get-multiplier-test
  (with-private-fns [score.tuning [get-multiplier]]
    (testing "get-multiplier"
      (is (= (/ 4 5) (get-multiplier "4/5"))) 
      (is (= 2 (get-multiplier "2"))) 
      (is (= (Math/pow 2 (/ 700.0 1200.0)) (get-multiplier "700.0"))) 
      )))

(deftest pch->freq-test 
  (let [twelve-tet-tune (partial pch->freq TWELVE-TET)] 
    (testing "pch->freq"
      (is (= (* MIDDLE-C 2.0) 
             (twelve-tet-tune [9 0])))
      (is (= (twelve-tet-tune [10 0])
             (twelve-tet-tune [8 24]))) 
      (is (= (twelve-tet-tune [7 5])
             (twelve-tet-tune [8 -7]))) 
      )))
