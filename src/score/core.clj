(ns score.core
  (:require 
    [clojure.string :refer  [join]]
    [score.util :refer [seq->gen]]))

(defn pch-add  [bpch interval]
  (let  [scale-degrees 12
         new-val  (+  (* scale-degrees  (first bpch))
                     (second bpch) interval)]
    [(quot new-val scale-degrees)
     (rem new-val scale-degrees)]))

(defn pch->sco  [[a b]]
  (format "%d.%02d" a b ))

(defn pch-interval-seq  [pch & intervals]
  (reduce  (fn  [a b]  (conj a  (pch-add  (last a) b)))  [pch] intervals))

(defn pch-interval-sco  [pch & intervals]
  (map pch->sco  (apply pch-interval-seq pch intervals)))

(defn- score-arg  [a]
  (if (number? a)
    (repeat a)
    a))

(defn gen-notes [& fields]
  (let [pfields  (map score-arg fields)]
    (apply map (fn [& args] args) pfields)))

(defn format-sco 
  "Formats a list of notes into a Csound SCO string"
  [notes]
  (join "\n"
        (map #(str "i" (join " " %)) notes)))

(defn gen-score [& fields]
  (format-sco (apply gen-notes fields)))



;; Score generators that take in the time of the event being generated 

(defn const 
  "Generates constant value"
  [val]
  (fn [t]
    val))

(defn rand-range 
  "Generates random value between low and high"
  [low high]
  (let [rng (- high low)] 
    (fn [t]
      (+ low (* (Math/random) rng)) 
      )))

(defn wrap-generator [f]
  (cond 
    (seq? f) (seq->gen f) 
    (fn? f) f
    :else (const f))) 

(defn gen-score2 
  [start dur & fields]
  (let [ gens (map wrap-generator fields) 
        [instrfn startfn & r] gens]
    (loop [cur-start 0.0 
           retval []]
      (if (< cur-start dur) 
        (let [i (instrfn cur-start)
              xt (startfn cur-start)
              note (into [i (+ start cur-start)] (map (fn [a] (a cur-start)) r))]
          (recur (+ cur-start xt) (conj retval note)))
        retval))))


