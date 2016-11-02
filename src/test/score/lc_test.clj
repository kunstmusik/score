(ns score.lc-test
  (:require [clojure.test :refer :all]
            [score.lc :refer :all]
            [score.freq :refer :all]
            ))

(def c4freq (sym->freq 'c4))

(deftest lc!-test

  (testing "basic usage"  
    (is (= (list [0.0 2.0 c4freq]) (lc! '(c4:2))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 1.0 c4freq]) 
           (lc! '(c4:2 c4)))))

  (testing "rests"
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 1.0]) 
           (lc! '(c4:2 r))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 4.0]) 
           (lc! '(c4:2 r:4))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 14.0]) 
           (lc! '(c4:2 r>16))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 1.0 c4freq]
                 [3.0 13.0]) 
           (lc! '(c4:2 c4 r>16)))))

  (testing "no rest using until-notation when duration passed"
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 14.0 c4freq]
                 ) 
           (lc! '(c4:2 c4:14 r>16)))))
  )

