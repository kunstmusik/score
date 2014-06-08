(ns ^{
      :author "Steven Yi"
      :doc "Probability functions. Based on Andre Bartetzki's CMask." } 
  score.probability
  (:require [score.core :refer [wrap-generator]])
  )

(defn uniform
  "uniform random generator with range 0.0 to 1.0"
  []
  (fn [t] (rand)))

(defn linear
  "linear distribution from 0.0 to 1.0 (if direction 1), or 1.0 to 0.0 (if 
  direction -1)" 
  [direction]
  (fn [t]
    (let [x1 (rand) 
          x2 (rand)]
      (if (> 0 direction)
        (if (< x1 x2) x1 x2)
        (if (> x1 x2) x1 x2)))))

(defn triangle
  "triangle distribution"
  []
  (fn [t] 
    (let [x1 (rand) 
          x2 (rand)]
      (* 0.5 (+ x1 x2)))))

(defn- rand-not [not-arg]
  (let [val (rand)]
    (if (= val not-arg)
      (recur not-arg)
      val
      )))

(defn exponential
  "exponential distribution
  direction - can be :decreasing, :increasing, or :bilateral
  lambda - affects exponential curvature (can be bpf, must be > 0.0)"
  [direction lambda]
  (let [lambdafn (wrap-generator lambda)]
    (if (= direction :bilateral)
      (fn [t]
        (let [x (* 2.0 (rand))]
          (if (> x 1.0)
            (loop [] 
              (let [a (- 2.0 x)
                    e (- (Math/log a))
                    ret (+ 0.5 (/ e 14.0 (lambdafn t)))]
                (if (or (> ret 1.0) (< ret 0))
                  (recur)
                  ret 
                  )))
            (loop [] 
              (let [e (Math/log x) 
                    ret (+ 0.5 (/ e 14.0 (lambdafn t)))]
                (if (or (> ret 1.0) (< ret 0))
                  (recur)
                  ret 
                  )))))) 
      (fn [t]
        (let [x (/ (- (Math/log (rand-not 0.0))) 7.0 (lambdafn t))] 
          (if (> x 1.0)
            (recur t)
            (if (= direction :increasing)
              (- 1.0 x)
              x
              )))))))


(defn gauss 
  "gaussian distribution, takes sigma and mu"
  [sigma mu]
  (let [sigmafn (wrap-generator sigma)
        mufn (wrap-generator mu)]
    (fn [t] 
      (let [sum (reduce +  (take 12  (repeatedly rand))) 
            e (+ (mufn t) (* (sigmafn t) (- sum 6.0)))]
        (if (or (> e 1.0) (< e 0.0))
          (recur t)
          e
          )))))


(defn cauchy 
  "cauchy distribution, takes sigma and mu"
  [alpha mu]
  (let [alphafn (wrap-generator alpha)
        mufn (wrap-generator mu)]
    (fn [t] 
      (loop [] 
        (let [x (rand-not 0.5) 
              e (+ (mufn t) (* (alphafn t) (Math/tan (* x Math/PI))))]
          (if (or (> e 1.0) (< e 0.0))
            (recur)
            e
            ))))))


(defn beta 
  "beta distribution, takes a {0..1} and b {0..1}, both can be bpf."
  [a b]
  (let [afn (wrap-generator a)
        bfn (wrap-generator b)]
    (fn [t] 
      (loop [] 
        (let [x1 (rand) 
              x2 (rand)
              yps1 (Math/pow x1 (/ 1.0 (afn t)))
              yps2 (Math/pow x2 (/ 1.0 (bfn t)))
              sum (+ yps1 yps2)]
          (if (> sum 1.0) 
            (recur)
            sum 
            ))))))

(defn weibull
  "weibull distribution, takes s {0..1} and t (>0), both can be bpf."
  [s t]
  (let [sfn (wrap-generator s)
        tfn (wrap-generator t)]
    (fn [t]
      (loop []
        (let [x (rand)
              a (/ 1.0 (- 1.0 x)) 
              e (* (sfn t) (Math/pow (Math/log a) (/ 1.0 (tfn t))))] 
          (if (> e 1.0)
            (recur)
            e
            ))))))
