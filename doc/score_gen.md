# Score Generation

Score's primary tool for generating notes is the gen-notes function:

```clojure
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
```

It works by using a given set of sequences, functions, or values, and generating a list using those values. This functionality is similar to using SuperCollider's [Patterns](http://doc.sccode.org/Tutorials/Getting-Started/16-Sequencing-with-Patterns.html) library.

## Example

The following example:

```clojure
(gen-notes 1 (range) 1.0 [1 2 3 4 5] (range 6 300))
```

When evaluated will generate the following list of lists:

```clojure
([1 0 1.0 1 6] 
 [1 1 1.0 2 7] 
 [1 2 1.0 3 8] 
 [1 3 1.0 4 9] 
 [1 4 1.0 5 10])                                                                              
```

In the call to gen-notes, the first and third argument are constants, 1 and 1.0.  These values are repeated for each generated note.  For the sequences used in the 2nd, 4th, and 5th argument, one value is used per-note, then the next values from those sequences is used, and so on.  Score generation is completed when the end of one of the sequences from the arguments has been reached. 

To note, because gen-notes uses map, the returned value is a lazy sequence. In general, one can use gen-notes as an infinite sequence, but more often than not one should use at least one finite-sequence as an argument to gen-notes.
