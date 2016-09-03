(ns
  ^{ :author "Steven Yi"
     :doc "Item list functions. Based on Andre Bartetzki's CMask." } 
  score.mask.items
  (:require [score.util :refer [seq->gen]])
  )

;; list items


(defn item-cycle
  "Cycles forwards through a sequence"
  [vs]
  (seq->gen (cycle vs)))

(defn swing
  "Cycles forwards and backwards through a sequence"
  [vs]
  (seq->gen 
    (cycle 
      (into vs (rest (reverse (rest vs)))))))

(defn heap
  "Generates values as random permutations of a sequence"
  [vs]
  (let [curval (atom (shuffle vs))]
    (fn [t]
      (let [[a & b] @curval]
        (if (seq? b)
          (reset! curval b) 
          (reset! curval (shuffle vs))) 
        a
        ))))


(defn rand-item 
  "Generates values as random permutations of a sequence"
  [vs]
  (fn [t]
    (rand-nth vs)))

