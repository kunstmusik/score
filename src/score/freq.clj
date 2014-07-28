(ns score.freq)

;; Functions for MIDI frequency

(defn midi->freq
  "Convert MIDI Note number to frequency in hertz"
  [notenum]
  (* 440 (Math/pow 2.0 (/ (- notenum 57) 12))))


;; Functions for keyword conversions to MIDI notenum

(def note-vals {\C 0 \D 2 \E 4 \F 5 \G 7 \A 9 \B 11} )

(defn- convert-modifier
  ([mod-str]
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

(defn pch->notenum  
  ([pch]
   (pch->notenum pch 12))
  ([[oct scale-degree] scale-degrees]
    (+  (* oct scale-degrees) scale-degree)))

;; functions related to PCH format: [oct pch]

(defn pch-add  
  "Add interval to pch (i.e. [8 0] 11 => [8 11]). Defaults to 12-tone octave."
  ([pch interval]
   (pch-add pch interval 12))
  ([pch interval scale-degrees]
  (let  [new-val  (+ (pch->notenum pch scale-degrees) interval)]
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
  (format "%d.%02d" a b ))

(defn pch-interval-seq  
  ([pch x]
   (if (sequential? x)
     (reductions pch-add pch x)   
     [pch (pch-add pch x)]))
  ([pch x & intervals]
   (pch-interval-seq pch (list* x intervals))))

(defn pch-interval-sco  [pch & intervals]
  (map pch->sco  (apply pch-interval-seq pch intervals)))

(defn analyze-intervals
  "Creates an interval list from a pch chord"
  [chord]
  (let [[x & xs] (map pch->notenum chord)]
    (first 
      (reduce (fn [[intervals last-pch] cur-pch]
                [(conj intervals  (- cur-pch last-pch)) cur-pch])
              [[] x] xs))))


;; Conversion to Hz

(defn hertz 
  [a]
  (cond 
    (keyword? a) (keyword->notenum a) 
    )
  )

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
