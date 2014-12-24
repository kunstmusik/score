# Amplitude

In Score, users will generally use one of three values: numeric value (dependent on output format, but generally 0.0-1.0), MIDI value (0-127), or decibels (where 0 dB equals 1.0).  For direct numeric values, no conversions are necessary.  

For MIDI values, Score currently does not offer a default value conversion function.  As far as I understand, the MIDI specification does not specify a standard formula, but simply that a logarithmic scale is suggested. Searching online shows a number of different mappings used. I will continue to research for a good mapping or set of mappings and will add them if a nice reusable set is found.

For decibels, Score contains the score.amp namespace includes the db-&gt;amp function. For example, if a user specifies amplitude values as the 4th field in the following:

```clojure

(def notes
  [['trumpet 0.0 0.1 -12 440]
   ['trumpet 0.2 0.1 -6 660]])

(println (process-notes notes 3 db->amp))
```

it would yield:

```clojure
([trumpet 0.0 0.1 0.251188643150958 440] 
 [trumpet 0.2 0.1 0.5011872336272722 660])
```
