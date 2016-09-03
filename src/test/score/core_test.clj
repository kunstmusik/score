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
