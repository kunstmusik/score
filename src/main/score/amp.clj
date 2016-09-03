(ns score.amp)

(def ^:private ^:const ^{:tag 'double} 
  LOG10D20 (/ (Math/log 10) 20))

(defn db->amp
  "Convert decibel to power ratio"
  [d] 
  (Math/exp (* (double d) LOG10D20)))

