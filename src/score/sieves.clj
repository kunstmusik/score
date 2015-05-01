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

(defmulti element? :tag)

(defmethod element? :union [s n]
  (let [sieves (:items s)]
    (some #(element? % n) sieves)))

(defmethod element? :intersection [s n]
   (let [sieves (:items s)]
    (every? #(element? % n) sieves)))

(defmethod element? :default [s n]
  (if-not (vector? s)
    (throw (Exception. (str "Invalid sieve: " s))))
  (let [[m i] s]
    (= i (mod n m))))


(defmulti normalize :tag)

(defmethod normalize :union [s]
  (apply U (map normalize (:items s))))

(defmethod normalize :intersection [s]
  (apply I (map normalize (:items s))))

(defmethod normalize :default [s]
  (let [[m i] s]
    [m (mod i m)]))

(defn- gcd
  "Greated Common Divisor"
  [a b]
  )

(defn- lcm 
  "Least multiple"
  [a b]
  )

(defn- euclide
  ;a1 >= a2 > 0
  [a1 a2]
  (loop [a1 a1 a2 a2]
    (let [tmp (mod a1 a2)] 
      (if (zero? tmp)
      a2 
      (recur a2 tmp)))))

(defn- meziriac 
  ;c1 >= c2 > 0
  [c1 c2]
  (if (= c2 1)
    1
    (loop [t 1]
      (if (= (mod (* t c1) c2) 1)
        t
        (recur (unchecked-inc t))))))

(defn gen-sieve 
  [sieve]
  (filter #(element? sieve %) (range)))


