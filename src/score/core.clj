(ns score.core
  (:require 
    [clojure.string :refer  [join]]
    [score.util :refer [seq->gen swapv!]]))

(defn- score-arg  [a]
  (cond (sequential? a) a 
    (fn? a) (repeatedly a)
    :default (repeat a)))

(defn gen-notes 
  "Generate notes by assembling sequences together into notes. 
  If a constant value is given, it will be wrapped with (repeat).
  If a no-arg function is given, it will be wrapped with (repeatedly)."
  [& fields]
  (let [pfields  (map score-arg fields)]
    (apply map (fn [& a] (into [] a)) pfields)))

(defn format-sco 
  "Formats a list of notes into a Csound SCO string"
  [notes]
  (join "\n"
        (map #(str "i" (join " " %)) notes)))

(defn gen-score 
  "Generates Csound score using gen-notes, then apply format-sco"
  [& fields]
  (format-sco (apply gen-notes fields)))


;; Score generators that take in the time of the event being generated 

(defn- const 
  "Generates constant value"
  [val]
  (fn [t]
    val))

(defn rand-range 
  "Generates random value between low and high"
  [low high]
  (let [rng (- high low)] 
    (fn [t]
      (+ low (* (Math/random) rng)) 
      )))

(defn wrap-generator [f]
  (cond 
    (seq? f) (seq->gen f) 
    (fn? f) f
    :else (const f))) 

(defn gen-notes2 
  "Generate notes with time-based generator functions. This score generation method
  is based on CMask. Given fields should be single-arg functions that generate a 
  value based-on time argument."
  [start dur & fields]
  (let [ gens (map wrap-generator fields) 
        [instrfn startfn & r] gens]
    (loop [cur-start 0.0 
           retval []]
      (if (< cur-start dur) 
        (let [i (instrfn cur-start)
              xt (startfn cur-start)
              note (into [i (+ start cur-start)] (map (fn [a] (a cur-start)) r))]
          (recur (+ cur-start xt) (conj retval note)))
        retval))))

(defn gen-score2
  [start dur & fields] 
  (format-sco (apply gen-notes2 start dur fields)))


(defmacro process-notes 
  "Process given notelist using pairs of index and functions. For example,
  using:
  
  (process-notes notes 
    3 db->amp
    4 keyword->freq)
 
  will process each note in notes, converting the 4th field with db->amp and
  5th field with keyword->freq.  Uses swapv! from pink.util."
  [notelist & body]
  (let [body (map (fn [[a b]] `(score.util/swapv! ~a ~b))
                    (partition 2 body))
        n (gensym)]
    `(map (fn [~n] 
            (-> ~n 
                ~@body))
          ~notelist)))

(defn with-tempo 
  "Adjust note start and durations according to tempo. 1.0 equals one beat at 
  tempo."
  ([tempo notes]
   (let [_notes (if (vector? (first notes)) notes [notes])
          adjust (/ 60.0 tempo)
          adjuster (partial * adjust)]
     (process-notes _notes
                    0 adjuster 
                    1 adjuster)))
  ([tempo note & notes]
   (with-tempo tempo (list* note notes))))


(defn with-start 
  "Translates note vectors in time by start value. Start may either be a double
  value, where inde of start time in notes is assumed to be 1, or start may
  also be given as a 2-vector in the form [index startime]."
  [start sco]
  (if (and (sequential? start) (= 2 (count start)))
    (let [[indx ^double start-time] start]
      (process-notes sco indx #(+ % start-time)))
    (process-notes 
      sco 
      1 #(+ % ^double start)))) 

(defn starting-at 
  "Given a starting time and given score, returns a note list where all notes with
  starting times earlier than the given start are dropped, and all remaining notes
  have their times translated relative to the start time.  Useful when developing 
  works to take a score start at a time somewhere in the middle, then send that off
  to a running engine."
  [start sco]
  (->> (filter #(>= (nth % 1) start) sco)
       (with-start (- start))))

;; measured scores

(defn get-measure-beat-length 
  "Gets beat length of measure given beat value and meter. I.e. for
  beat = 4, and meter of 4 4, returns 4 beats as the measure length."
  [meter-num meter-beats]
  (* meter-num (/ 4 meter-beats)))

(defn convert-measured-score
  "Converts a measured score into a single score list. The measured-score
  is a single list that has the following shape:

  [:meter 4 4 
    0 [list0 list1] 
    1 [list0] 
    2 [list0 list1]]

  This means, with a meter of 4/4, read each pair of values as a measure number
  and list of note lists. For example, list0 is played at measure 0, 1, and 2,
  while list1 is played only in measures 0 and 2.  If list1 had only one note
  of [instr-func 0.0 1.0], then the generated score would have:
 
  [[instr-func 0.0 1.0] [instr-func 8.0 1.0]] 

  This notation allows for organizing hierarchies of materials as lists
  of notes. Note, notes within note lists must be in the format of:
  
  [instr-func start-time duration optional-args...]"
  [score]
  (let [[m meter-num meter-beats & measures] score]
    ;; verify args
    (cond 
      (not= :meter m)
      (throw (Exception. "Invalid Score Header: Must be of form [:meter x x] where x are numbers"))
      (odd? (count measures))
      (throw (Exception. "Invalid Score Measures: Count must be even number of x-[] pairs")))
    (let [measure-beats (get-measure-beat-length meter-num meter-beats)]
      (mapcat (fn [[m s]]
                (let [start (* m measure-beats)]
                  (mapcat #(with-start start %) s)))
              (partition 2 measures)))))
