# Beats - functions for beat sequences and sets

score.beats provides conversion functions for creating beat sequences (sequences of 1's and 0's) and beat sets.  

## Design

score.beats deals with notation of beat patterns.  This is different from generation of patterns, such as in score.euclid or other pattern generation methods. Users using score.beats will be notating out the beat hits using sequences or strings. The score.beats functions will in turn convert the input from users into data structures appropriate for processing. The different notation methods each have their own advantages and disadvantages and the goal for score.beats is to leave the method to use up to the user. 

## Terminology

A **beat sequence** (or *pat*) is a sequence of 1's and 0's, where 1's denote a hit and 0's denote a silence. The index into the sequence maps to the beat number for which a 1 or 0 occurs. 

A **beat set** is a set whose members are beat indices where hits occur. As sets are also functions, a set can be used with a beat index to check if that index exists within the set. The presence/absence of the beat index within the set denotes a hit or silence. 

A **beat string** is like a beat sequence but in string form.  

A **hex string** is a string of hexadecimal characters where each character represents four beats worth of data.  For example, 0 = 0000 and f = 1111.  


## Examples

```clojure

;; Manually written beat sequence. Each beat is a 16th note.
(def pat '(1 0 0 0 1 0 0 0 1 0 0 0 1 0 0 0))

;; The same using a vector
(def pat2 [1 0 0 0 1 0 0 0 1 0 0 0 1 0 0 0])

;; The same pattern notated as a beat string. 
(def beat-str "1000100010001000")

;; The same pattern notated as hex string.
(def hex-str "8888")

;; The same pattern notated as beat set.
(def hex-str #{0 4 8 12})

;; Conversion to beat sequence from beat and hex string.
(def pat3 (str->pat beat-str))
(def pat4 (hex->pat hex-str))

;; Conversion to sets
(def set1 (pat->set pat))
(def set2 (pat->set pat2))
(def set3 (pat->set pat3))
(def set4 (pat->set pat4))

;; use of sets
(defn perf [beat]
  (when (set1 beat)
    (perform-something x y z)))

;; direct notation of sets

(defn perf [beat]
  (when (#{0 4 8 12} beat) 
    (perform-something x y z)))

;; Direct conversion to sets
(defn perf [beat]
  (let [bd (hex->set "8888")
        snare (hex->set "0808")
        cym (hex->set "ffff")]
    (when (bd beat)
      (perf-bd 0.5))
    (when (snare beat)
      (perf-snare 0.5))
    (when (cym beat)
      (perf-cym 0.5)))
```

## Comments

The following are personal observations:

* Notation of beat sequences and beat strings shows clearly what happens on every beat, but may take time to notate, especially in a live coding context. Modifying a sequence may also take time to count each beat index to find the right beat to modify.  With sequences, one has some formatting options to make it quicker to scan and modify a sequence (shown below). However, it also takes up more visual space/lines of text, meaning less can be viewed on the screen at one time. 

```clojure
(def pat 
  [1 0 0 0
   1 0 0 0
   1 0 0 0
   1 0 0 0])
```

* Notation of hex strings is very compact. It assumes working in groupings of 4 beats/sub-beats, which may not work for all user requirements. Requires practice to have the hex value's mapping to beats become second nature.  Audience members unfamiliar with hexadecimal may also have no clue what is going on.  Besides being compact, it is fast to mutate the total beat sequence.

* Direct set writing is also very quick to write and is a sparse notation of what should occur on what beats.  By writing only what beat indices a beat should trigger, it can be quick to mutate as not much thinking required. For example, if one now wants to trigger on beats 114 and 16, one just adds 14 and 16 to the set definition.  Being sparse it also takes up little visual room.  On the other hand, as more of the beat structure fills up, the notation of the set starts to take up more space than a beat sequence or beat string.
