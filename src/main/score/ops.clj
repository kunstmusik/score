(ns score.ops
  "Score operation functions for translating, scaling, repeating, and merging
  serial and parallel parts. Also for calculating durations. Assumption about
  score note format are described in each function's documentation." 
  )


(defn sco-dur
  "Calculates score block duration. Assumes 2nd and 3rd items in each Note 
  represent start time and duration."
  [sco]
  (double 
    (reduce
    #(max (+ (nth %2 1) (nth %2 2)) %)
    0.0
    sco)))

(defn |+|
  "Adjust start times for score. Assumes 2nd item in each Note represents
  start time."
  [sco ^double start]
  (map #(assoc % 1 (+ start (nth % 1))) sco))

(defn |*|
  "Repeat score for num-repeat times every duration in time."
  ([sco num-repeat dur]
   (|*| sco num-repeat dur 0.0))
  ([sco num-repeat dur start]
  (if (> num-repeat 1)
    (concat (|+| sco start)
            (|*| sco (dec num-repeat) dur (+ start dur)))
    (|+| sco start))))

(defn |s|
  "Flattens list of score blocks into single list in series, adjusting 
  start times for each event in score block to follow previous score block.
  If an item within the sco-blocks is a number, it will advance the start time
  by that number (useful to put in space between score blocks)."
  [& sco-blocks]
  (if (< (count sco-blocks) 2)
    sco-blocks
    (first 
      (reduce
        (fn [[sco start] sco-block] 
          (if (number? sco-block)
            [sco (+ start sco-block)]
            [(concat sco (|+| sco-block start)) 
             (+ start (sco-dur sco-block))]))
        [[] 0.0]
        sco-blocks))))

(defn |p|
  "Flattens list of score blocks into a single list in parallel. (Simply 
  concatenates blocks.)"
  [& sco-blocks]
  (apply concat sco-blocks))


(defn |scale|
  [sco ^double multiplier]
  (map #(->
          %
         (assoc 1 (* multiplier (nth % 1))) 
         (assoc 2 (* multiplier (nth % 2))))
  sco))

(defn |scale-to|
  [sco dur]
  (|scale| sco (/ dur (sco-dur sco))))
