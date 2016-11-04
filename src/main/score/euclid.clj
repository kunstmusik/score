(ns score.euclid
  "Function for generating Euclidean rhythm patterns.
 
  Based on 'The Euclidean Algorithm Generates Traditional Musical Rhythms'
  by Godfried Toussaint

  and Lisp code by Ruin and Wesen:

  https://web.archive.org/web/20131114124454/http://ruinwesen.com/blog?id=216
  
  "  
  )

(defn rotate-seq
  "Rotates sequence by x steps."
  [steps xs]
  (if (zero? steps) 
    xs
    (let [v (mod steps (count xs))] 
      (concat (drop v xs) (take v xs)))))

(defn- reduce-euclid
  [h t]
  (loop [[a & as] h
        [b & bs] t
        coll []]
    (if (not (or a b))
      coll
      (recur as bs (conj coll (concat a b))))))

(defn- euclid-process
  [coll]
  (let [[h t] (split-with #{(first coll)} coll)]
    (if (<= (count t) 1)
      (flatten coll)
      (euclid-process (reduce-euclid h t)))))

(defn euclid
  "Generates Euclidean Beat pattern given number of hits per total steps.
  Returns a sequence of 1s and 0s where 1 is a hit and 0 is silent."
  ([hits steps] (euclid hits steps 0))
  ([hits steps rotation]
   (let [base
         (concat (repeat hits [1]) 
                 (repeat (- steps hits) [0]))]
     (rotate-seq rotation (euclid-process base)))))

