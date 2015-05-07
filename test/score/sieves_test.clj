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

(deftest gcd-test
  (with-private-fns [score.sieves [gcd]] 
    (testing "GCD using Euclid's Algorithm"
    (is (= 4 (gcd 16 4)))
    (is (= 21 (gcd 252 105)))
    (is (= 21 (gcd 105 252)))
    ))
  )

(deftest reduce-sieve-test
  (testing "reduce-sieve"
    (is (= [4 3] (reduce-sieve (I [4 3] nil))))
    (is (= [4 3] (reduce-sieve (I nil [4 3]))))
    (is (= [4 3] (reduce-sieve (I [4 3] [4 3]))))
    (is (= [8 3] (reduce-sieve (I [8 3] [4 3]))))
    (is (nil? (reduce-sieve (I [8 3] [8 4]))))

    (is (= [4 3] (reduce-sieve (U [4 3] nil))))
    (is (= [4 3] (reduce-sieve (U nil [4 3]))))
    (is (nil? (reduce-sieve (U nil (I [8 3] [8 4])))))
    (is (nil? (reduce-sieve (U (I [8 3] [8 4]) nil))))
    ))


