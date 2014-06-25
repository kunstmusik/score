(ns score.core
  (:require 
    [clojure.string :refer  [join]]
    [score.util :refer [seq->gen]]))

(defn- score-arg  [a]
  (cond (seq? a) a 
    (fn? a) (repeatedly a)
    :default (repeat a)))

(defn gen-notes 
  "Generate notes by assembling sequences together into notes. 
  If a constant value is given, it will be wrapped with (repeat).
  If a no-arg function is given, it will be wrpaped with (repeatedly)."
  [& fields]
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

(defn gen-notes2 
  "Generate notes with time-based generator functions. This score generation method
  is based on CMask. Given fields should be single-arg functions that generate a 
  value based-on time argument."
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

(defn gen-score2
  [start dur & fields] 
  (format-sco (apply gen-notes2 start dur fields)))
