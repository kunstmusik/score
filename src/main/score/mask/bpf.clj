(ns 
  ^{ :author "Steven Yi"
     :doc "Break-point functions. Based on Andre Bartetzki's CMask." } 
  score.mask.bpf)

(defn- find-points 
  [t pts]
  (if (< t (ffirst pts))
    [(first pts) nil] 
    (loop [[a & b] pts]
      (if (seq? b)
        (if (and (<= (first a) t) (> (ffirst b) t))
          [a (first b)] 
          (recur b))
        [a nil]))))

(defn- interp-linear 
  [t [x1 y1] [x2 y2]]
  (let [m (/ (- t x1) (double (- x2 x1)))
        x (- y2 y1)]
    (+ y1 (* m x))))

(defn- interp-exponential 
  [t ^double exp [x1 y1 :as pt1] [x2 y2 :as pt2]]

  (let [m (/ (- t x1) (double (- x2 x1)))]
   (cond
    (= 0.0 t)
      (interp-linear t pt1 pt2)
    (and (pos? exp) (>= y2 y1))
      (+ y1 (* (- y2 y1) (Math/pow m (+ exp 1.0))))
    (and (pos? exp) (< y2 y1))
      (+ y2 (* (- y1 y2) (Math/pow (- 1.0 m) (+ exp 1.0))))
    (and (neg? exp) (>= y2 y1))
      (+ y2 (* (- y1 y2) (Math/pow (- 1.0 m) (+ (Math/abs exp) 1.0))))
    (and (neg? exp) (< y2 y1))
      (+ y1 (* (- y2 y1) (Math/pow m (+ (Math/abs exp) 1.0))))
    ))
  )

(defn- interp-cos
  [t [x1 y1] [x2 y2]]
  (let [m (/ (- t x1) (double (- x2 x1)))
        cx (+ 0.5 (/ (Math/cos (+ Math/PI (* Math/PI m))) 2.0))
        x (- y2 y1)
        ]
    (+ y1 (* cx x))))

(defn bpf
  "Creates break-point generator functions. Optional :ipl arg can be
  :linear (the default), positive or negative number (for exponential curves), 
  :cos (for half-cosine), or :none (for no interpolation)."
  [pts & {:keys [ipl] :or {ipl :linear}}]

  (when (odd? (count pts))
    (throw (Throwable. "bpf requires an even number of points"))
    )
  (let [pairs (partition 2 pts)]
    (cond
      (= ipl :linear)
      (fn [t]
        (let [[a b] (find-points t pairs)]
          (if (seq? b)
            (interp-linear t a b)
            (second a)
            )))
      (number? ipl)
      (fn [t]
        (let [[a b] (find-points t pairs)]
          (if (seq? b)
            (interp-exponential t ipl a b)
            (second a)
            )))
      (= ipl :cos)
      (fn [t]
        (let [[a b] (find-points t pairs)]
          (if (seq? b)
            (interp-cos t a b)
            (second a)
            )))
      (= ipl :none)
      (fn [t]
        (let [[a b] (find-points t pairs)]
          (second a)))
      :else
      (throw (Throwable. (str "Unknown interpolation type: " ipl))))))

