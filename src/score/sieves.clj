(ns score.sieves
  "\"...sieve theory is the study of the internal symmetries of a series of points 
  either constructed intuitively, given by observation, or invented completely 
  from moduli of repetition.\" - Xenakis, Formalized Music, pp. 276 
  
  Sieve functions based on work by Iannis Xenakis. Sieves are represented as
  a 2-vector of [modulo index].  U and I are used to create Union and
  Intersection Sieves. Sieves may also be nil, though are factored out when a
  Sieve is simplified, and generates no sequence when gen-sieve is called.

  
 Code Consulted:
   * C Code from \"Sieves\" article below.
   * Haskell Music Theory (https://hackage.haskell.org/package/hmt-0.15) 
   * athenaCL (http://www.flexatone.org/athena.html) 
 
 Literature:
   * Xenakis and Rahn. \"Sieves\". Perspectives of New Music, Vol. 28, No. 1
   (Winter, 1990), pp. 58-78.
   * Xenakis. \"Formalized Music\". pp. 268-288.
  "
  (:require [clojure.java.io :refer :all]
            [clojure.string :refer [trim triml split]]))

;; NOTES
;; 

;; UTILITY FUNCTIONS

(defn- euclid 
  "Euclid's algorithm for computing the greatest common divisor"
  [a1 a2]
  (loop [a1 a1 a2 a2]
    (let [tmp (mod a1 a2)] 
      (if (zero? tmp)
      a2 
      (recur a2 tmp)))))

;(defn- lcm 
;  "Least multiple"
;  [a b]
;  (/ (* a b) (euclid a b)))

(defn- meziriac 
  [c1 c2]
  (if (= c2 1)
    1
    (loop [t 1]
      (if (= (mod (* t c1) c2) 1)
        t
        (recur (unchecked-inc t))))))

(defn- reduce-intersection
  [s1 s2]
  (let [[m1 i1] s1
        [m2 i2] s2
        d (euclid m1 m2)
        i1' (mod i1 m1)
        i2' (mod i2 m2)
        c1 (/ m1 d)
        c2 (/ m2 d)
        m3 (* d c1 c2)
        t (meziriac c1 c2)
        i3 (mod (+ i1' (* t (- i2' i1') c1)) m3)]
    (if (and (not= d 1) (not= (mod (- i1' i2') d) 0))
      nil
      [m3 i3])))

;; Sieve is represented as vector of [m i] where m is the modulus and
;; i is the initial offset

(defprotocol Sieve 
  "Protocol for Sieves"
  (element? [s n] "Determines if number n is an element of this Sieve")
  (reduce-sieve [s] "Reduces the Sieve to its simplest form")
  (normalize [s] "Reduces Sieves in [mod index] form to its normalized form"))

(defn- reduce-aggregate
  [aggregate-fn l r]
  (let [l' (reduce-sieve l)
        r' (reduce-sieve r)
        s' (aggregate-fn l' r')]
    (cond
      (nil? l') r'
      (nil? r') l'
      (and (= l l') (= r r')) s'
      :default (reduce-sieve s'))))

(defrecord Union [l r]
  Sieve
  (element? [s n]
    (or (element? l n) (element? r n)))
  (reduce-sieve [s]
    (reduce-aggregate ->Union l r))
  (normalize [s]
    (Union. (normalize l) (normalize r))))

(defrecord Intersection [l r]
  Sieve 
  (element? [s n]
    (and (element? l n) (element? r n)))
  (reduce-sieve [s]
    (cond
      (nil? r) l
      (= l r) l
      (and (vector? l) (vector? r))
      (reduce-intersection l r)
      :default
      (reduce-aggregate ->Intersection l r)))
  (normalize [s]
    (Intersection. (normalize l) (normalize r))))

(extend-type nil
  Sieve
  (element? [s n] false)  
  (reduce-sieve [s] nil)
  (normalize [s] nil))

(extend-type clojure.lang.PersistentVector 
  Sieve
  (element? [s n] 
    (let [[m i] s] 
      (= i (mod n m))))  
  (reduce-sieve [s] s)
  (normalize [s] 
    (let [[m i] s] 
      [m (mod i m)])))

(defn U 
  "Create new sieve that is the union of sieves"
  [& sieves]
  (reduce ->Union sieves))

(defn I
  "Create new sieve that is the intersection of sieves"
  [& sieves]
  (reduce ->Intersection sieves))

(defn simplified 
  "Returns simplified (normalized and reduce) version of sieve"
  [sieve]
  (reduce-sieve (normalize sieve)))

(defn gen-sieve 
  "Generate sequence using sieve. Can optionally provide number n of elements
  to produce."
  ([n sieve]
   (take n (gen-sieve sieve)))
  ([sieve]
  (let [s (simplified sieve)]
    (when-not (nil? s)
      (filter #(element? s %) (range))))))

