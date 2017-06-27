# Score Transformation 

Since notes are generic lists of data, the Score library has no explicit
knowledge about what is in a note. It does not know if a field in a note
is a PCH, a frequency, an amplitude, or other value. This limits the
library from providing fixed operations such as transposing or
stretching notes.

However, a different approach is used in Score. The
process-notes macro allows a given note list to be
transformed in a generic fashion. It takes in a single note list and
then pairs of indexes and transformation functions. It processes the
note list such that for each note, the values at the given indexes will
be given to the corresponding transformation functions. The transformed
field value is then used in the resulting transformed note list.

```clojure
    (def notes
      [['trumpet 0 1 -12 :G5]
       ['trumpet 1 1 -12 :B5]
       ['trumpet 3 1 -12 :D6]])

    (process-notes notes
      3 db->amp
      4 keyword->freq)
```

The above shows an example use of
process-notes. It reads as "given the notes
note list, process each note, converting the 4th field from decibels to
amplitude multipliers and the 5th field from keywords to frequencies".
Note that the indexes are 0-based, so 0 refers to the first field, 1 to
the second field, and so on. The results of processing are shown below.

```clojure
    ([trumpet 0 1 0.251188643150958 783.9908719634985]
     [trumpet 1 1 0.251188643150958 987.7666025122485]
     [trumpet 3 1 0.251188643150958 1174.6590716696305])
```

This example shows one way of approaching score transformation, which is
to allow writing note values in a form that is convenient to the user
but transforming the values into one more suitable for signal processing
routines. As the transformation functions provided are generic,
process-notes can also be used to implement musical
operations such as transpositions, decrescendos, time stretching, and so
on.

process-notes provides a generic way to transform scores.
As users are in control of specifying the meaning of field values for
notes, users must also have a way to specify transformations by field.
By providing transformation functions, the user is acknowledging they
know what a field means as well as how they would like it be
transformed.

For more complex transformations of scores, the processing model of
process-notes may not be enough. However, as note lists are
generic list data structures, users can avail themselves of Clojure’s
standard list processing functions to implement their own custom
transformations.


# Additional Operations

score.ops provides additional score manipulation operations that do make
assumptions about the structure of notes within a score, particularly that the
2nd field is a start time and the 3rd field is a duration. (The use of start
and duration as fields 2 and 3 correspond to usage in Music-N systems,
particularly the pfield system in Csound.)

The following operations are available:

* sco-dur - Calculates score duration
* |+| - Adjust start times (translates) for score
* |\*| - Repeats score for given number of time, translating start times in
  repeated parts.
* |s| - Flattens given score blocks into a single score list, composing the
  score blocks serially. Users can provide numbers in between score blocks to
  advance score time (i.e., put space between notes).
* |p| - Flattens given score blocks into a single score list, composing the
  score blocks in parallel.  
* |scale| - Scales start/dur for each note in score according to given 
  multiplier.
* |scale-to| - Scales start/dur for each note in score according to given 
  target duration for entire score block.  

