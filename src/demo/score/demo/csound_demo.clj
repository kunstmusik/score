(ns score.demo.csound-demo
  (:require [score.core :refer :all]
            [score.mask.probability :refer :all]
            [score.mask.items :refer :all])
  (:import [csnd6 csnd6 Csound CsoundPerformanceThread] 
           [java.util Random]))

(def orc "
sr=44100
ksmps=32
nchnls=2
0dbfs=1

instr 1 
ipch = cps2pch(p5, 12)
kenv linsegr 0, .05, 1, .05, .7, .4, 0
aout vco2 p4 * kenv, ipch 
aout moogladder aout, 2000, 0.25
outs aout, aout
endin")

(defn fire-sco [pt]
  (.InputMessage pt (gen-score2 0 10.0
                            1 0.5 0.4 "0.25" "8.00" 
                            )))

(comment

  ;; Eval below to Start
  (csnd6/csoundInitialize (bit-or csnd6/CSOUNDINIT_NO_ATEXIT csnd6/CSOUNDINIT_NO_SIGNAL_HANDLER))

  (def ^Csound cs (Csound.))
  (def ^CsoundPerformanceThread pt (CsoundPerformanceThread. cs))
  (.SetOption cs "-odac") 
  (.CompileOrc cs orc) 
  (.Start cs)            
  (.Play pt)
  ;; end initial start eval

  ;; eval below to fire score
  (.InputMessage pt (gen-score2 0 60.0
                                1 
                                (gauss 0.5 0.1)
                                (heap [0.1 0.2 0.4])
                                (rand-range 0.1 0.25)
                                (rand-item 
                                  ["8.00" "8.03" "8.02"] )
                                ))

  ;; eval below to stop csound
  (.InputMessage pt "e")
  (.Join pt)
  (.Stop cs)
  (.Cleanup cs)
  )
