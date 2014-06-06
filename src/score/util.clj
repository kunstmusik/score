(ns score.util)

(defn seq->gen 
  "Converts a sequence into a generator function with time arg"
  [vs]
  (let [curval (atom vs)] 
    (fn [t]
      (let [[a & b] @curval]
        (swap! curval rest) 
        a
        ))))

(defn debug-print
  "Decorates a generator function. Prints the generated value and passes through the value."
  [f]
  (fn [t]
    (let [v (f t)]
      (println ">>> " v) 
      v
      )))
