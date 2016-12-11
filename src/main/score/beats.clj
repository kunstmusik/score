(ns score.beats
  "Functions for working with beat patterns and sets.")

(defn str->pat
  "Converts beat string of 1's and 0's to sequence of 1's and 0's."
  [beat-str]
  (let [num-one {\1 1 \0 0}]
    (map num-one beat-str)))

(defn pat->set
  "Converts a sequence of 1's and 0's into a beat set whose
  members includes the indices in the sequence where 1's are
  found. The set may then be used as a function to test
  whether a beat number should trigger an action or not."
  [pat]
  (loop [[x & xs] pat
         indx 0
         res #{}]
    (if x
      (recur xs (inc indx)
        (if (= 1 x)
          (conj res indx)
          res))
      res)))

(defn str->set
  "Converts beat string into beat set."
  [beat-str]
  (pat->set (str->pat beat-str)))

(defn hexchar->str
  "Converts a hexadecimal char to 4-char binary string representation."
  [c]
  (as-> (Long/parseLong (str c) 16) $
    (Long/toBinaryString $) 
    (format "%4s" $)
    (.replace $ \space \0)))

(defn hex->str
  "Converts a hexadecimal string to binary string representation."
  [hex-str]
  (apply str (map hexchar->str hex-str)))

(defn hex->pat
  "Converts a hexadecimal string into a beat sequence of 1's and 0's. Each hex digit within the string generates 4 beats of values.  0 = 0000, 1 = 0001, 2 = 0010, ... f = 1111."
  [hex-str]
  (str->pat (hex->str hex-str)))

(defn hex->set
  "Converts a hexadecimal string into a beat set."
  [hex-str]
  (pat->set (hex->pat hex-str)))

