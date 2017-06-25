(ns score.sieves-test
  (:require [clojure.test :refer :all]
            [score.sieves :refer :all]))

(defmacro with-private-fns  [[ns fns] & tests]
    "Refers private fns from ns and runs tests in context."
      `(let ~(reduce #(conj %1 %2 `(ns-resolve '~ns '~%2))  [] fns)
              ~@tests))


(deftest gen-sieve-test
  (testing "Generation of sequences with sieves"
    (is (= [0 2 4 6] (take 4 (gen-sieve [2 0])))) 
    (is (= [1 2 3 5 7 8 9 11 13 14] 
           (take 10 (gen-sieve (U [2 1] [3 2]))))) 
    (is (= [5 11 17 23 29 35 41 47 53 59] 
           (take 10 (gen-sieve (I [2 1] [3 2]))))) 
    (is (nil? (gen-sieve (I [2 1] [2 2])))) 
    (is (= [5 11 17 23 29 35 41 47 53 59] 
           (take 10 
                 (gen-sieve (U (I [2 1] [3 2])
                                  (I [2 1] [2 2])
                                  )))))
    (is (= [0 3 6 8 11 14] 
           (gen-sieve 6 (apply U (map #(vector 8 %) [0 3 6])))))
    (is (= [8 32 56] (gen-sieve 3 (I [3 2] [8 0]))))
    (is (= [0 1 4 7 8 10 12 13 16 19 20 22] 
           (gen-sieve 12 (U [3 1] [4 0]))))
    (is (= [2 3 4 5 8 9 10 11 14 17 19 20 23 24] 
           (gen-sieve 14 (U [5 4] [3 2] [7 3]))))
    (is (= [2 3 10 12 17 22 24 31]
           (gen-sieve 8 (U (I [5 2] [2 0]) [7 3]))))
    (is (= [3 11 23 33 35 47 59 63 71 83]
           (gen-sieve 10 (U 
                          (I [3 2] [4 7]) 
                          (I [6 9] [15 18])))))
    (is (= [3 23 33 47 63 70 71 93 95 119]
           (gen-sieve 10 (U [24 23] [30 3] [104 70]))))
    ))

(deftest normalize-test
  (testing "Normalize"
    (is (= [4 1] (normalize [4 5])) ) 
    (is (= [11 0] (normalize [11 22])) ) 

    (is (= (U [3 0] [4 3]) 
           (normalize (U [3 9] [4 7])))) 

    (is (= (I [4 1] (U [3 0] [4 3])) 
           (normalize (I [4 5] (U [3 9] [4 7]))))) 
    ))

(deftest euclid-test
  (with-private-fns [score.sieves [euclid]] 
    (testing "GCD using Euclid's Algorithm"
    (is (= 4 (euclid 16 4)))
    (is (= 21 (euclid 252 105)))
    (is (= 21 (euclid 105 252)))
    ))
  )

(deftest reduce-sieve-test
  (testing "reduce-sieve"
    (is (= nil (reduce-sieve (I [4 3] nil))))
    (is (= nil (reduce-sieve (I nil [4 3]))))
    (is (= nil (reduce-sieve (I [4 2] [4 3]))))
    (is (= [4 3] (reduce-sieve (I [4 3] [4 3]))))
    (is (= [8 3] (reduce-sieve (I [8 3] [4 3]))))
    (is (nil? (reduce-sieve (I [8 3] [8 4]))))

    (is (= [4 3] (reduce-sieve (I [4 3] (I [4 3] [4 3])))))
    (is (= nil (reduce-sieve (I [4 3] (I [4 3] [4 2])))))

    (is (= [4 3] (reduce-sieve (U [4 3] nil))))
    (is (= [4 3] (reduce-sieve (U nil [4 3]))))
    (is (nil? (reduce-sieve (U nil (I [8 3] [8 4])))))
    (is (nil? (reduce-sieve (U (I [8 3] [8 4]) nil))))
    ))

(deftest analyze-sieve-test
  (testing "analyze sieve"
    (is 
      (= { :analysis [[30 3 4] [12 11 8]]
          :sieve (U [30 3] [12 11])
          :period 60 }
         (analyze-sieve [3 11 23 33 35 47 59 63 71 83 93 95]))))
  (testing "gen/analyze roundtrip"
    (let [sieve (U [30 3] [12 11])] 
      (is (= sieve 
             (:sieve
               (analyze-sieve (gen-sieve 12 (U [30 3] [12 11]))))
             )))))
