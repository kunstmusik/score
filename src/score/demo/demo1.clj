(ns score.demo.demo1
  (:require 
    [score.core :refer :all]
    [score.freq :refer :all]
    [score.mask.probability :refer :all]
    [score.mask.items :refer :all]
    ))


;; Test Code

(def score
  (gen-score 1 0 1
             (pch-interval-sco  [6 0] 12 8 6 2 6)
             (pch-interval-sco  [6 0] 12 8 6 2 6)
             (range -10 -100 -1) 0 1))

(println "[TEST 1]")
(println score)


(def score2
  (gen-score2 0.0 10.0 
              4 0.5 3 
              (rand-range 0.1 20)
              (cycle [1 2 3]) 
              (swing [8 9 10])
              (heap [10 100 400]) 
              (rand-item [50 500 5000]) 
              ))

(println "[TEST 2]")
(println  score2)

(def score3
  (gen-score2 0.0 10.0 3 0.25 
              (uniform) 
              (linear 1) 
              (linear -1)
              (triangle)              
              (exponential :increasing 2.0)              
              (exponential :decreasing 1.0 )              
              (exponential :bilateral 0.7 )              
              ))

(println "[TEST 3]")
(println score3)


(println "[TEST 4]")
(println (gen-score2 0.0 10.0 4 0.5 
                     (gauss 0.5 0.2)
                     (cauchy 0.1 0.5)
                     (beta 0.1 0.1)
                     (weibull 0.5 2.0)
                     ))
