(ns score.lc-test
  (:require [clojure.test :refer :all]
            [score.lc :refer :all]
            [score.freq :refer :all]
            ))

(def c4freq (sym->freq 'c4))

(deftest lc!-test

  (testing "basic usage"  
    (is (= (list [0.0 2.0 c4freq]) (lc! '(c4:2))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 1.0 c4freq]) 
           (lc! '(c4:2 c4)))))

  (testing "rests"
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 1.0]) 
           (lc! '(c4:2 r))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 4.0]) 
           (lc! '(c4:2 r:4))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 14.0]) 
           (lc! '(c4:2 r>16))))
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 1.0 c4freq]
                 [3.0 13.0]) 
           (lc! '(c4:2 c4 r>16)))))

  (testing "no rest generated using until-notation when duration passed"
    (is (= (list [0.0 2.0 c4freq]
                 [2.0 14.0 c4freq]
                 ) 
           (lc! '(c4:2 c4:14 r>16)))))

  (testing "octave carry"
    (is (= (list [0.0 1.0 c4freq])
                 (lc! '(c4))))
    (is (= (list [0.0 1.0 c4freq])
                 (lc! '(c))))
    (is (= (list [0.0 1.0 c4freq])
                 (lc! '(c))))
    (is (= (list [0.0 1.0 c4freq]
                 [1.0 1.0 (* 2 c4freq)]
                 [2.0 1.0 c4freq])
                 (lc! '(c c5 c4))))
    (is (= (list [0.0 1.0 (sym->freq 'c5)]
                 [1.0 2.0 (sym->freq 'd5)]
                 [3.0 1.0 (sym->freq 'eb5)])
                 (lc! '(c5 d:2 eb)))))

  (testing "relative octaves"

    (is (= (list [0.0 1.0 c4freq])
           (lc! '(c4))))
    (is (= (list [0.0 1.0 c4freq]
                 [1.0 1.0 (hertz 'c5)])
           (lc! '(c c+))))
    (is (= (list [0.0 2.0 (hertz 'c5)]
                 [2.0 1.0 c4freq])
           (lc! '(c5:2 c-)))))
 
 (testing "until-notation for notes"
    (is (= (list [0.0 1.0 (hertz 'c5)]
                 [1.0 3.0 (hertz 'c5)])
         (lc! '(c5 c5>4))))
    (is (= (list [0.0 4.0 (hertz 'c5)])
         (lc! '(c5:4 c5>4)))))
 


 (testing "chords"
    (is (= (list [0.0 1.0 (hertz 'c5)]
                 [1.0 3.0 [(hertz 'c5) 
                           (hertz 'g5)]])
         (lc! '(c5 [c5>4 g] )))))
    (is (= (list 
                 [0.0 4.0 [(hertz 'c5) 
                           (hertz 'g5)]]
                 [4.0 1.0 (hertz 'c5)])
         (lc! '([c5>4 g] c))))
    
    (is (= (list 
                 [0.0 4.0 [(hertz 'c5) 
                           (hertz 'g6)]]
                 [4.0 1.0 (hertz 'c5)])
         (lc! '([c5>4 g+] c))))
    )

