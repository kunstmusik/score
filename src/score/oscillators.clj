(ns ^{:author "Steven Yi"
      :doc "Oscillator Generators, based on Andre Bartetzki's CMask" } 
  score.oscillators
  (:require [score.core :refer [wrap-generator]])
  )

(def PI2 (* Math/PI 2.0))

(defn- get-phase 
  [phaseinit t freq]
  (+ phaseinit (* t freq)))

(defn sin 
  "Sine-wave generator"
  ([]
   (sin 1.0 0.0))
  ([freq]
   (sin freq 0.0))
  ([freq phase]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (+ 0.5 
          (* 0.5 
             (Math/sin (* PI2 (get-phase phase t (freqfn t))))))))))


(defn cos 
  "Cosine-wave generator"
  ([]
   (sin 1.0 0.0))
  ([freq]
   (sin freq 0.0))
  ([freq phase]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (+ 0.5 
          (* 0.5 
             (Math/cos (* PI2 (get-phase phase t (freqfn t))))))))))


(defn saw-up 
  "Sawtooth (upwards) generator"
  ([]
   (saw-up 1.0 0.0))
  ([freq]
   (saw-up freq 0.0))
  ([freq phase]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (Math/abs (rem (get-phase phase t (freqfn t)) 1.0))
       ))))

(defn saw-down 
  "Sawtooth (downwards) generator"
  ([]
   (saw-up 1.0 0.0))
  ([freq]
   (saw-up freq 0.0))
  ([freq phase]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (- 1.0 (Math/abs (rem (get-phase phase t (freqfn t)) 1.0)))))))

(defn power-up
  "Power up generator"
  ([]
   (power-up 1.0 0.0 1.0))
  ([freq]
   (power-up freq 0.0 1.0))
  ([freq phase]
   (power-up freq phase 1.0))
  ([freq phase exponent]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (Math/pow (Math/abs (rem (get-phase phase t (freqfn t)) 1.0))
                 (Math/pow 2.0 exponent))))))

(defn power-down 
  "Power down generator"
  ([]
   (power-down 1.0 0.0 1.0))
  ([freq]
   (power-down freq 0.0 1.0))
  ([freq phase]
   (power-down freq phase 1.0))
  ([freq phase exponent]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (Math/pow (- 1.0 (Math/abs (rem (get-phase phase t (freqfn t)) 1.0)))
                 (Math/pow 2.0 exponent))))))

(defn square 
  "Square-wave generator"
  ([]
   (square 1.0 0.0))
  ([freq]
   (square freq 0.0))
  ([freq phase]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (let [x (Math/abs (rem (get-phase phase t (freqfn t)) 1.0))]  
         (if (< x 0.5) 1.0 0.0))))))

(defn triangle 
  "Triangle-wave generator"
  ([]
   (triangle 1.0 0.0))
  ([freq]
   (triangle freq 0.0))
  ([freq phase]
   (let [freqfn (wrap-generator freq)] 
     (fn [t]
       (let [x (Math/abs (rem (get-phase phase t (freqfn t)) 1.0))]  
         (if (< x 0.5)
           (* 2 x)
           (* 2 (- 1.0 x))))))))

