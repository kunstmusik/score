(ns score.lc
  "Mini-language for music notation. Originally developed for live coding."
  (:require [score.core :refer :all]
            [score.freq :refer :all]
            [clojure.string :refer [blank?]]
            ))

(defn- dots 
  "Count dots and returns rest of sequence. Returns [num-dots rest]."
  [symlist]
  (loop [num-dots 0 syms (seq symlist)]
    (if syms
     (let [[x & xs] syms]
      (if (= '. x)
        (recur (inc num-dots) xs)
        (vector num-dots syms))) 
      (vector num-dots nil))))


(def restsym
  #"([rR])(([:>])(\d+))?")

(def notesym
  #"([a-gA-G][sSbB]?)([\d+-])?(:(\d+))?")

(defn lc!
  "Compiles musical symbol list into musical value list output.
  Output will contains vectors in the form:

  [start dur freq]

  If the output is a rest, the vector will have the form

  [start dur]
  "
  ([symlist] (lc! symlist 4 {:start 0.0 :dur 1.0}))
  ([symlist oct state]
 
   (let [[x & xs] symlist
         start (:start state)
         dur (:dur state)
         v [start dur]]
     (cond 
       (nil? x) nil

       (number? x)
       (lc! xs oct (assoc state :dur x))

       (re-matches restsym (name x))
       (let [[_ _ _ ^String v-type ^String v] (re-matches restsym (name x))
             d (case v-type
                ">" (- (Long/parseLong v) start)
                ":" (* dur (Long/parseLong v))
                 nil dur)]
         (if (pos? d)
          (cons [start d] 
                (lazy-seq (lc! xs oct (assoc state :start (+ start d)))))
          (lc! xs oct state)))

       (re-matches notesym (name x))
       (let [[_ ^String n ^String found-oct _ ^String v] 
             (re-matches notesym (name x))
             d (if v (* dur (Long/parseLong v)) dur)
             new-state (assoc state :start (+ start d))
             new-oct (if (not (blank? found-oct)) 
                       (case found-oct
                         "+"  (inc oct)
                         "-" (dec oct)
                         (Long/parseLong found-oct)) 
                       oct)
             note-name (str n new-oct)]
         (cons [start d (str->freq note-name)] 
               (lazy-seq (lc! xs new-oct new-state))))

       :else
       (throw (Exception. (str "Unexpected symbol: " x)))
       )) 
    ))

