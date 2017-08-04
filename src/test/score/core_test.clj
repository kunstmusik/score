(ns score.core-test
  (:require [clojure.test :refer :all]
            [score.core :refer :all]))

;(defn bass-drum [] ())

;(def pattern
;  [['bass-drum 0.0 0.5]
;   ['bass-drum 1.0 0.5]
;   ['bass-drum 2.0 0.5]
;   ['bass-drum 3.0 0.5]])

;(def score
;  [0.0 pattern
;   4.0 pattern])

;(println (convert-timed-score score))



;(def bd-pattern
;  [['bass-drum 0.0 0.5]
;   ['bass-drum 1.0 0.5]
;   ['bass-drum 2.0 0.5]
;   ['bass-drum 3.0 0.5]])

;(def snare-pattern
;  [['snare-drum 1.0 0.5]
;   ['snare-drum 3.0 0.5]])

;(def score
;  [0.0 bd-pattern 
;   4.0 (process-notes bd-pattern 2 #(* % 0.5)) 
;       snare-pattern
;       [['single-shot-sample 2.0 2.0]]])

;(println (convert-timed-score score))


;(def score
;  [:meter 4 4
;  0 bd-pattern 
;  1 bd-pattern snare-pattern])

;(println (convert-measured-score score))

(deftest convert-timed-score-test
  (testing "Simple timed-score"
    (is (= [[:a 1.0] 
          [:a 2.0]
          [:b 3.0]
          [:b 4.0]]
         (convert-timed-score 
           [1 [[:a 0] [:a 1]]
            3 [[:b 0] [:b 1]] ]))))
  (testing "Nested timed-score"
    (is (= [[:a 1.0]
            [:a 2.0]
            [:b 3.0]
            [:b 4.0]
            [:c 5.0]
            [:c 6.0]
            ]
           (convert-timed-score
             [1 [[:a 0] [:a 1]]
              3 [0 [[:b 0] [:b 1]]
                 2 [[:c 0] [:c 1]] ]
              ]
             ))))
  (testing "Passes through notelists"
    (is (= [[:a 1]]
           (convert-timed-score [[:a 1]])))))


(deftest repeat-seq-test
  (testing "repeat-seq"
    (is (= '(3 3 3) (repeat-seq 3 [3])))
    (is (= '(3 4 3 4 3 4 3 4) (repeat-seq 4 [3 4])))))
