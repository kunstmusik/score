(ns ^{
      :author "Steven Yi"
      :doc "Quantize. Based on Andre Bartetzki's CMask." } 
  score.quantizer
  (:require [score.core :refer [wrap-generator]])
  )

;; Note: Documentation for quantize comes from Andre Bartetzki's CMask Manual

(defn quantize 
  "A quantizer attracts the results of the preceeding modul onto a quantization 
  grid q. The optional strength s  {0...1} (default 1) specifies to which 
  extent the grid takes effect (1 is total quantization, 0 is no quantization 
  at all). The optional offset o (default 0) shifts the whole grid by the given 
  amount. Each of the 3 parameters may be constant or time variant (bpf). "
  ([g genfn]
   (quantize g 1 0 genfn))
  ([g s genfn]
   (quantize g s 0 genfn))
  ([g s o genfn]
   (let [gfn (wrap-generator g)
         sfn (wrap-generator s)
         ofn (wrap-generator o)]
    (fn [t]
      (let [offset (ofn t)
            gridsize (gfn t)
            strength (sfn t)

            d (- (genfn t) offset)
            r (Math/floor (/ (+ d (/ gridsize 2.0) ) gridsize))
            err (- (/ d gridsize) r)
            ]
        (+ offset (* gridsize (+ r (* err (- 1 strength))))))))))
