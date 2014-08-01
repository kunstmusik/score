(ns score.demo.demo3
  (:require 
    [score.core :refer :all]
    [score.bpf :refer :all]
    [score.util :refer [debug-print]]
    ))


;; Test Code

(defn gen-test-score 
  [instr fn1]
  (gen-score2 0.0 2.0 
              instr 0.1
              fn1))

(println "[OSCILLATOR TEST]\n")
(println "\n[TEST BPF LINEAR]")


(println (gen-test-score 1 
                         (bpf [0 0 1.0 5.0])
                         ))



(println (gen-test-score 2 
                         (bpf [0 0 1.0 5.0] :ipl 2.0)
                         ))

(println (gen-test-score 2.5
                         (bpf [0 5.0 1.0 0] :ipl 2.0)
                         ))

(println (gen-test-score 3 
                         (bpf [0 0 1.0 5.0] :ipl -2.0)
                         ))

(println (gen-test-score 3.5
                         (bpf [0 5.0 1.0 0.0] :ipl -2.0)
                         ))

(println (gen-test-score 4 
                         (bpf [0 0 1.0 5.0] :ipl :cos)
                         ))

(println (gen-test-score 5 
                         (bpf [0 0 1.0 5.0] :ipl :none)
                         ))
