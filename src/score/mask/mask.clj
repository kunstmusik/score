(ns ^{
      :author "Steven Yi"
      :doc "Mask function. Based on Andre Bartetzki's CMask." } 
  score.mask.mask
  (:require [score.core :refer [wrap-generator]]))

(defn- mask-value 
  [low high value exp]
  (if (= exp 0.0)
    (+ low (* (- high low) value))  
    (+ low (* (- high low) (Math/pow value exp)))))

(defn mask
  "Maps values from a generator function to a range given by 
  low and high. Low and high may be numbers or bpf's. Optional 
  :exp key/val argument affects the exponent for the mapping. 
  Mapping follows y = x ^(2^exponent)."
  ([low high genfn]
   (mask low high genfn 0.0))
  ([low high genfn exp]
  (let [lowfn (wrap-generator low)
        highfn (wrap-generator high)]
    (fn [t]
      (let [l (lowfn t)
            h (highfn t)]
        (mask-value l h (genfn t) exp))))))
