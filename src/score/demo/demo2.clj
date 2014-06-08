(ns score.demo.demo2
  (:require 
    [score.core :refer :all]
    [score.oscillators :refer :all]
    [score.util :refer [debug-print]]
    ))


;; Test Code

(defn gen-test-score 
  [instr fn1 fn2]
  (gen-score2 0.0 2.0 
              (const instr) (const 0.1) 
              fn1 fn2))

(println "[OSCILLATOR TEST]\n")
(println "\n[TEST SIN/COS]")
(println (gen-test-score 1 (sin 0.5 0.5) (cos 2.0 0.5)))

(println "\n[TEST SAW UP/DOWN]")
(println (gen-test-score 1 (saw-up 2.0 0.5) (saw-down 2.0 0.5)))

(println "\n[TEST SQUARE/TRIANGLE]")
(println (gen-test-score 1 (square 3.0) (triangle 3.0 0.5)))

(println "\n[TEST POWER UP/DOWN]")
(println (gen-test-score 1 (power-up 1.5 0.5) 
                         (power-down 1.5 0.5)))
