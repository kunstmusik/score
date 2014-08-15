(ns score.tuning
  "Functions related to tunings, based Scala scale file format."
  (:require [clojure.java.io :refer :all]
            [clojure.string :refer [trim triml split]]))

(def MIDDLE-C 261.625565)
(def TWELVE-TET
  { :description "Twelve-Tone Equal Temperament"
    :base-freq MIDDLE-C 
    :num-scale-degrees 12
    :octave 2.0
    :ratios (map #(Math/pow 2.0 (/ % 12)) (range 12))
   })

(defn- remove-comments 
  [s]
  (first (split (triml s) #"\s+")))

(defn- convert-cents
  [s]
  (let [v (Double/parseDouble s)]
    (Math/pow 2 (/ v 1200.0))))

(defn- convert-ratio
  [s]
  (let [[a b] (split (trim s) #"/")]
    (if b 
      (/ (Integer/parseInt a) (Integer/parseInt b)) 
      (Integer/parseInt a))))

(defn- get-multiplier
  "Returns multiplier depending on if string value is ratio or cents. Cents
  include a . in the string, all else are considered ratios."
  [s]
  (let [v (remove-comments s)]
    (if (>= (.indexOf ^String v ".") 0)
      (convert-cents v)
      (convert-ratio v))))

(defn create-tuning-from-file 
  "Creates a tuning using a Scale scale file. 

  See http://www.huygens-fokker.org/scala/scl_format.html for more information."
  [scala-scale-file-name]
  (with-open [rdr (reader scala-scale-file-name)]
    (loop [tuning {} 
           state 0
           [x & xs] (line-seq rdr)
           ratios [1.0]
           ]
      (if x
        (cond 
          (.startsWith ^String x "!") (recur tuning state xs ratios) 
          (= state 0) (recur (assoc tuning :description (trim x)) 
                                   (inc state) xs ratios)
          (= state 1) (let [pch-count (Integer/parseInt (trim x))]
                        (recur (assoc tuning :num-scale-degrees pch-count)
                               (inc state) xs ratios))
          (= state 2) (if (= (:num-scale-degrees tuning) (count ratios))
                        (recur (assoc tuning :octave (get-multiplier x))
                               (inc state) xs ratios)
                        (recur tuning state xs 
                               (conj ratios (get-multiplier x)))) 
          :else (recur tuning state xs ratios) 
          )
        (assoc tuning :ratios ratios)))))

(defn pch->freq 
  "Convert a pch (i.e. [oct scale-degree]) to frequency using
  the given tuning. Octave of 8 is interpreted as equal to 
  the base frequency of the tuning."
  [tuning [oct scale-degree]]
  (let [num-scale-degrees (:num-scale-degrees tuning)
        oct-adj (+ oct (quot scale-degree num-scale-degrees))
        scale-degree-adj (rem scale-degree num-scale-degrees)] 
    (loop [o oct-adj s scale-degree-adj]
      (if (neg? s)
        (recur (dec o) (+ num-scale-degrees s))
        (let [multiplier (Math/pow (:octave tuning) (- o 8))]
          (* (* multiplier (:base-freq tuning)) 
             (nth (:ratios tuning) s))))))) 

