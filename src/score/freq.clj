(ns score.freq)

;; Functions for MIDI frequency

(defn midi->freq
  "Convert MIDI Note number to frequency in hertz"
  [notenum]
  (* 440 (Math/pow 2.0 (/ (- notenum 69) 12))))


;; Functions for keyword conversions to MIDI notenum

(def note-vals {\C 0 \D 2 \E 4 \F 5 \G 7 \A 9 \B 11} )

(defn- convert-modifier
  ([^String mod-str]
   (let [mod-len (.length mod-str)] 
     (loop [v 0 indx 0]
       (if (>= indx mod-len)
         v
         (let [c (get mod-str indx)]
           (case c
             \B (recur (+ v -1) (inc indx))
             \# (recur (+ v 1) (inc indx))
             (throw (Throwable. (str "Unknown note modifier: " c))))))))))

(defn keyword->notenum
  "Convert keyword to MIDI notenum (i.e. :C4 is 60, :C#4 is 61)"
  [sym]
  {:pre [(keyword? sym)]}
  (let [sym-str (.toUpperCase (name sym))
        sym-len (.length sym-str)
        note-name (get sym-str 0)
        note (note-vals note-name)
        modifier (.substring sym-str 1 (- sym-len 1)) 
        octave (Integer/parseInt (.substring sym-str (- sym-len 1))) ]
    (+ note (* 12 (- octave 4)) 60 (convert-modifier modifier)) 
    ))

(defn keyword->freq
  "Convert keyword to frequency (i.e. :A4 is 440.0)"
  [sym]
  (midi->freq (keyword->notenum sym)))

(defn pch->notenum  
  "Converts PCH notation to note number. Optional number of
  scale degrees per octave, defaults to 12."
  ([pch]
   (pch->notenum pch 12))
  ([[oct scale-degree] scale-degrees]
    (+ (* oct scale-degrees) scale-degree)))

(defn pch->freq 
  "Converts PCH to frequency. [8 0] is equivalent to Middle-C (MIDI Note 60),
  which is equivalent to :C4.  Assumes 12 scale degrees per octave (12 TET)."
  ([pch]
   (midi->freq (- (pch->notenum pch 12) 36))))


(defn hertz 
  "General function for converting value a to frequency.  It will convert
  keywords, integers, PCH, and return any other value."
  [a]
  (cond 
    (keyword? a) (keyword->freq a) 
    (integer? a) (midi->freq a) 
    (and (sequential? a) (= 2 (count a))) (pch->freq a) 
    :else a))


;; functions related to PCH format: [oct pch]

(defn pch-add  
  "Add interval to pch (i.e. [8 0] 11 => [8 11]). Defaults to 12-tone octave."
  ([pch interval]
   (pch-add pch interval 12))
  ([pch interval scale-degrees]
  (let [new-val (+ (pch->notenum pch scale-degrees) interval)]
    [(quot new-val scale-degrees)
     (rem new-val scale-degrees)])))

(defn pch-diff 
  "Return the interval between two pitches (i.e. [8 0] [9 1] => 13). Defaults
  to 12-tone octave."
  ([pch1 pch2]
   (pch-diff pch1 pch2 12))
  ([pch1 pch2 scale-degrees]
  (- (pch->notenum pch2 scale-degrees) (pch->notenum pch1 scale-degrees))))

(defn pch->sco  [[a b]]
  "Converts PCH notation to Csound-style pch string."
  (format "%d.%02d" a b ))

(defn pch-interval-seq  
  "Given a base PCH pch and sequence of intervals x, generates a sequence of PCH's.  For
  example, (pch-interval-seq [8 0] [1 -2]) will yield ([8 0] [8 1] [7 11])."
  ([pch x]
   (pch-interval-seq pch x 12))
  ([pch x scale-degrees]
   {:pre [(sequential? x)]}
   (reductions #(pch-add %1 %2 scale-degrees) pch x))) 

(defn pch-interval-sco  
  "Given a base PCH pch and sequence of intervals x, generates a sequence of Csound pch strings.  For
  example, (pch-interval-seq [8 0] [1 -2]) will yield (\"8.0\" \"8.01\" \"7.11\")." 
  ([pch intervals]
    (pch-interval-sco pch intervals 12))
  ([pch intervals scale-degrees]
  (map pch->sco (pch-interval-seq pch intervals scale-degrees)))
  )

(defn analyze-intervals
  "Creates an interval list from a pch chord"
  [chord]
  (let [[x & xs] (map pch->notenum chord)]
    (first 
      (reduce (fn [[intervals last-pch] cur-pch]
                [(conj intervals  (- cur-pch last-pch)) cur-pch])
              [[] x] xs))))

;; Functions for intervals
;; interval path vs. interval set...
(defn invert 
  "Inverts a list of pchs given the version number"
  [[base-pch & more :as pchs] inversion]
  {:pre [(not (neg? inversion)) (< inversion (count pchs))]}
  (if (= 0 inversion)
    pchs
    (let [inv-point (- (count pchs) inversion)]
      (map #(if (< % inv-point)
              %2 
              (let [oct1 (first base-pch) 
                    oct2 (first %2)]
                  (pch-add %2 (* -12 (+ 1 (* 2 (- oct2 oct1))))) 
                )
              )
           (range (count pchs))
           pchs) )))
