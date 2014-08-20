(ns score.orchestra.performer
  "Functions for modeling Performers (based on Steven Yi's Python Orchestral 
  Composition Library).  Uses 8 pfield format:
  
  p1 - instrument 
  p2 - start time
  p3 - duration
  p4 - pch1 
  p5 - pch2
  p6 - amplitude
  p7 - space (Left: -1.0, Right: 1.0)
  p8 - articulation
 
  Score note should leave out p1, to be filled in by Performer.  
  "
  (:require [score.tuning :refer [pch->freq]]
            [score.util :refer [swapv!]]))

(defn create-performer
  ([instr amp-adj space & {:keys [name tuning] 
                       :or {name "Performer" tuning nil}}] 
  {:name name
   :instr instr
   :amp-adj amp-adj
   :space space
   :tuning tuning
   }))

(defn- csound-note-perform
  [performer note]
  (let [v (-> (into [(:instr performer)] note)
              (swapv! 2 #(- % (* (Math/random) 0.01)))
              (swapv! 5 #(* (:amp-adj performer) %))
              (swapv! 6 (fn [a] (:space performer))))
        tuning (:tuning performer)]
    (if tuning
      (let [tune (partial pch->freq tuning)] 
        (-> v 
            (swapv! 3 tune)
            (swapv! 4 tune)))
      v)))

;(def p (create-performer 1 0.95 0.1 :name "Test"))
;(csound-note-perform p [0.0 2.0 [8 0] [8 0] -12 0.1 0])

(defn perform 
  "Default performance method for Csound-based scores (8 pfields)"
  [performer notes]
  (map #(csound-note-perform performer %) notes))

