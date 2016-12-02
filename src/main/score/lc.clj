(ns score.lc
  "Mini-language for music notation. Originally developed for live coding."
  (:require [score.core :refer :all]
            [score.freq :refer :all]
            [clojure.string :refer [blank?]]
            ))

(def ^:private restsym
  #"([rR])(([:>])(\d+))?")

(def ^:private notesym
  #"([a-gA-G][sSbB]?)([\d+-])?(([:>])(\d+))?")

(def ^:private just-notesym
  #"([a-gA-G][sSbB]?)([\d+-])?")


;; TODO - refactor out note symbol and rest processing

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

       (vector? x)
       (let [[y & ys] x
             [_ ^String n ^String found-oct 
                            _ ^String v-type ^String v] 
             (re-matches notesym (name y))
             d (case v-type
                 ">" (- (Long/parseLong v) start)
                 ":" (* dur (Long/parseLong v))
                 nil dur) 
             new-state (assoc state :start (+ start d))
             new-oct (if (not (blank? found-oct)) 
                       (case found-oct
                         "+"  (inc oct)
                         "-" (dec oct)
                         (Long/parseLong found-oct)) 
                       oct)
             note-name (str n new-oct)

             rest-names 
             (loop [oct new-oct
                    [p & ps] ys
                    out []]
               (if p
                 (let [[_ ^String n ^String found-oct]
                       (re-matches just-notesym (name p))
                       new-oct (if (not (blank? found-oct)) 
                                 (case found-oct
                                   "+"  (inc oct)
                                   "-" (dec oct)
                                   (Long/parseLong found-oct)) 
                                 oct)
                       note-name (str n new-oct)
                       ]
                    (recur new-oct ps (conj out note-name))) 
                 out))]
           (if (pos? d)
            (cons [start d (mapv str->freq (into [note-name] rest-names))] 
                 (lazy-seq (lc! xs new-oct new-state))) 
            (lc! xs new-oct state)) 

         )

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
       (let [[_ ^String n ^String found-oct 
              _ ^String v-type ^String v] 
             (re-matches notesym (name x))
             d (case v-type
                ">" (- (Long/parseLong v) start)
                ":" (* dur (Long/parseLong v))
                 nil dur) 
             new-state (assoc state :start (+ start d))
             new-oct (if (not (blank? found-oct)) 
                       (case found-oct
                         "+"  (inc oct)
                         "-" (dec oct)
                         (Long/parseLong found-oct)) 
                       oct)
             note-name (str n new-oct)]
         (if (pos? d)
          (cons [start d (str->freq note-name)] 
               (lazy-seq (lc! xs new-oct new-state))) 
          (lc! xs new-oct state)))

       :else
       (throw (Exception. (str "Unexpected symbol: " x)))
       )) 
    ))

