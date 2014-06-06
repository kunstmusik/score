(ns score.probability)

(defn uniform
  "uniform random generator with range 0.0 to 1.0"
  []
  (fn [t] (rand)))

(defn linear
  "linear distribution from 0.0 to 1.0 (if direction 1), or 1.0 to 0.0 (if direction -1)" 
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
   lambda - affects exponential curvature"
  [direction lambda]
  (if (= direction :bilateral)
    (fn [t]
      (let [x (* 2.0 (rand))]
        (if (> x 1.0)
          (loop [] 
            (let [a (- 2.0 x)
                e (- (Math/log a))
                ret (+ 0.5 (/ e 14.0 lambda))]
              (if (or (> ret 1.0) (< ret 0))
                (recur)
                ret 
                )))
          (loop [] 
            (let [e (Math/log x) 
                ret (+ 0.5 (/ e 14.0 lambda))]
              (if (or (> ret 1.0) (< ret 0))
                (recur)
                ret 
                )))))) 
    (fn [t]
      (let [x (/ (- (Math/log (rand-not 0.0))) 7.0 lambda)] 
        (if (> x 1.0)
          (recur t)
          (if (= direction :increasing)
            (- 1.0 x)
            x
            ))))))


(defn gauss 
  "gaussian distribution, takes sigma and mu"
  [sigma mu]
  (fn [t] 
    (let [sum (reduce +  (take 12  (repeatedly rand))) 
        e (+ mu (* sigma (- sum 6.0)))]
      (if (or (> e 1.0) (< e 0.0))
        (recur t)
        e
        ))))


(defn cauchy 
  "cauchy distribution, takes sigma and mu"
  [alpha mu]
  (fn [t] 
    (loop [] 
      (let [x (rand-not 0.5) 
          e (+ mu (* alpha (Math/tan (* x Math/PI))))]
      (if (or (> e 1.0) (< e 0.0))
        (recur)
        e
        )))))


(defn beta 
  "beta distribution, takes a and b"
  [a b]
  (fn [t] 
    (loop [] 
      (let [x1 (rand) 
            x2 (rand)
            yps1 (Math/pow x1 (/ 1.0 a))
            yps2 (Math/pow x2 (/ 1.0 b))
            sum (+ yps1 yps2)]
        (if (> sum 1.0) 
          (recur)
          sum 
          )))))

(defn weibull
  "weibull distribution, takes s and t"
  [s t]
  (fn [t]
    (loop []
      (let [x (rand)
            a (/ 1.0 (- 1.0 x)) 
            e (* s (Math/pow (Math/log a) (/ 1.0 t)))] 
        (if (> e 1.0)
          (recur)
          e
          )))))
