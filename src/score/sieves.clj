(ns score.sieves
  "Functions related to Xenakis Sieves"
  (:require [clojure.java.io :refer :all]
            [clojure.string :refer [trim triml split]]))

;; Sieve is represented as vector of [m i] where m is the modulus and
;; i is the initial offset


(defn U 
  "Create new sieve that is the union of sieves"
  [& sieves]
  {:tag :union
   :items sieves})

(defn I
  "Create new sieve that is the intersection of sieves"
  [& sieves]
  {:tag :intersection
   :items sieves})

(defmulti sieve? :tag)

(defmethod sieve? :union [s n]
  (let [sieves (:items s)]
    (some #(sieve? % n) sieves)))

(defmethod sieve? :intersection [s n]
   (let [sieves (:items s)]
    (every? #(sieve? % n) sieves)))

(defmethod sieve? :default [s n]
  (if-not (vector? s)
    (throw (Exception. (str "Invalid sieve: " s))))
  (let [[m i] s]
    (= i (mod n m))))

(defn gen-sieve 
  [sieve]
  (filter #(sieve? sieve %) (range)))


;; (take 20 (gen-sieve (U  [3 2]  [4 3])))

;(defn period
;  [sieve]
;  )


