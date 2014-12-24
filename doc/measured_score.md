# Measured and Timed Scores

With a bottom-up approach to music making, one might first start with building up small units of music.  This could be a drum pattern, a melodic line, a granular cloud, a processed sample of sound, or more.  Once smaller units are created, it is natural to build up larger units of music, which in turn may be used to build up even larger units of music. This framework or approach to music making has been used long before computers, and numerous approaches have been taken within computer music.  (For an example of a historic system that has been very effective and inspiring--at least, in my opinion--please see [HMSL](http://www.softsynth.com/hmsl/).)

Score offers two primary functions for higher level organization of music: convert-timed-score and convert-measured-score. The two functions take in list data structures in a specified format, process according to specified times, and yield a flattened note list. The two functions operate similarly with the exception of how on works with time specifications.  They are describd with examples below.

## convert-timed-score

convert-timed-score allows one to organize smaller blocks of score into a larger score.  The user specifies a list of values that can be either numbers or note lists.  If a number is encountered, it sets the current time to translate a note list by; if a note list is encountered, it will be translated in time by the current time.  For note lists, convert-timed-score requires that individual note's second value be a specification for a start time. For example, the following score:

```clojure
(def pattern
  [['bass-drum 0.0 0.5]
   ['bass-drum 1.0 0.5]
   ['bass-drum 2.0 0.5]
   ['bass-drum 3.0 0.5]])

(def score
  [0.0 pattern
   4.0 pattern])

(println (convert-timed-score score))
```

would yield the following score:

```clojure
([bass-drum 0.0 0.5] 
 [bass-drum 1.0 0.5] 
 [bass-drum 2.0 0.5] 
 [bass-drum 3.0 0.5] 
 [bass-drum 4.0 0.5] 
 [bass-drum 5.0 0.5] 
 [bass-drum 6.0 0.5] 
 [bass-drum 7.0 0.5])
```

In the example, the score variable contains the timed score.  It reads as "at time 0.0, play pattern, and at time 4.0, play the pattern again". To note, users can specify multiple items to start at a given time.  For example, if the above is extended to the following:

```clojure
(def bd-pattern
  [['bass-drum 0.0 0.5]
   ['bass-drum 1.0 0.5]
   ['bass-drum 2.0 0.5]
   ['bass-drum 3.0 0.5]])

(def snare-pattern
  [['snare-drum 1.0 0.5]
   ['snare-drum 3.0 0.5]])

(def score
  [0.0 bd-pattern 
   4.0 bd-pattern snare-pattern])

(println (convert-timed-score score))
```

The resulting processed score would be:

```clojure
([bass-drum 0.0 0.5] 
 [bass-drum 1.0 0.5] 
 [bass-drum 2.0 0.5] 
 [bass-drum 3.0 0.5] 
 [bass-drum 4.0 0.5] 
 [bass-drum 5.0 0.5] 
 [bass-drum 6.0 0.5] 
 [bass-drum 7.0 0.5] 
 [snare-drum 5.0 0.5] 
 [snare-drum 7.0 0.5])
```

Since the notes lists are just lists, users can hand-write blocks of notes, use note-processing functions, and use note-generating functions within a timed score. For example:

```clojure

(def score
  [0.0 bd-pattern 
   4.0 (process-notes bd-pattern 2 #(* % 0.5)) 
       snare-pattern
       [['single-shot-sample 2.0 2.0]]])

(println (convert-timed-score score))
```

would yield:

```clojure
([bass-drum 0.0 0.5] 
 [bass-drum 1.0 0.5] 
 [bass-drum 2.0 0.5] 
 [bass-drum 3.0 0.5] 
 [bass-drum 4.0 0.25] 
 [bass-drum 5.0 0.25] 
 [bass-drum 6.0 0.25] 
 [bass-drum 7.0 0.25] 
 [snare-drum 5.0 0.5] 
 [snare-drum 7.0 0.5] 
 [single-shot-sample 6.0 2.0])
```

In the example above, the second use of bd-pattern has been processed with the process-notes function, such that the 3rd field of each note has its value multiplied by 0.5 (i.e. has its duration shortened by half).  Also, a single-shot-sample note has been introduced to the score, written in by hand. 

With convert-timed-score, the user is free to organize pre-written blocks of notes in time. They can also use note generation functions and introduce hand-written notes as well.  

## convert-measured-score


