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

;; functions related to PCH format: [oct pch]

(defn pch-add  [bpch interval]
  (let  [scale-degrees 12
         new-val  (+  (* scale-degrees  (first bpch))
                     (second bpch) interval)]
    [(quot new-val scale-degrees)
     (rem new-val scale-degrees)]))

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


;; Conversion to Hz

(defn hertz 
  [a]
  (cond 
    (keyword? a) (keyword->notenum a) 
    )
  )
