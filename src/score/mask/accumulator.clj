(ns 
  ^{ :author "Steven Yi"
     :doc "Accumulator. Based on Andre Bartetzki's CMask." } 
  score.mask.accumulator
  (:require [score.core :refer [wrap-generator]]))

;; Documentation for accumulate taken from Andre Bartetzki's CMask Manual

(defn accumulate 
  "The accumulator continuously sums all its input values to an inital value 
  init. The inital value is optional and has a default value of 0."
  ([genfn]
   (accumulate genfn 0))
  ([genfn init]
   (let [_genfn (wrap-generator genfn)
         accum (atom init)] 
     (fn [t]
       (swap! accum + (_genfn t))))))

(defn limit [v l h]
  (cond
    (= l v) l
    (< v l) l
    (> v h) h
    :default v))

(defn create-accum-function
  [genfn low high init accumfn]
  (let [_genfn (wrap-generator genfn) 
        accum (atom init)
        lowfn (wrap-generator low)
        highfn (wrap-generator high)]
    (fn [t]
      (let [h (highfn t)
            l (lowfn t)]
        (swap! accum
               (fn [a b] (accumfn (+ a b) l h)) 
               (_genfn t))))))

(defn accumulate-limit 
  "The accumulator continuously sums all its input values to an inital value 
  init. The inital value is optional and has a default value of 0."
  ([low high genfn]
   (accumulate-limit low high genfn 0))
  ([low high genfn init]
   (create-accum-function genfn low high init limit)))

(defn mirror 
  ; value, low, high, range
  [v l h]
  (let [r (- h l)]
    (cond 
      (zero? r) l
      (> v h) (- h (rem (- v l) r))
      (< v l) (+ l (rem (- h v) r))
      :default v
      ) 
    ) 
  )

(defn accumulate-mirror
  "The accumulator continuously sums all its input values to an inital value 
  init. The inital value is optional and has a default value of 0."
  ([low high genfn]
   (accumulate-mirror low high genfn 0))
  ([low high genfn init]
   (create-accum-function genfn low high init mirror)))


(defn wrap [v l h]
  ; value, low, high, range
  [v l h]
  (let [r (- h l)]
    (cond 
      (zero? r) l
      (> v h) (+ l (rem (- v l) r)) 
      (< v l) (- h (rem (- h v) r)) 
      :default v)))

(defn accumulate-wrap
  "The accumulator continuously sums all its input values to an inital value 
  init. The inital value is optional and has a default value of 0."
  ([low high genfn]
   (accumulate-wrap low high (wrap-generator genfn) 0))
  ([low high genfn init]
   (create-accum-function (wrap-generator genfn) low high init wrap)))
