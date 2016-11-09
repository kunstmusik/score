# Musical Values

Score provides a number of functions for generating and converting
musical values. These functions are useful on their own as well as when
generating and processing notes. These value functions are described
below.

#### Amplitude and Frequency

The following lists basic functions provided by Score for conversion between
different values for both amplitude and frequency.

| Function | Description | 
| -------- | ----------- |
| db->&gt;amp | converts decibels to power ratios. |
| midi-&gt;freq | converts MIDI note numbers to frequency (Hz). |
| keyword-&gt;notenum | convert pitch keywords to MIDI note numbers. |
| keyword-&gt;freq | convert pitch keyword to frequency (Hz). |
| pch-&gt;notenum | convert PCH format to MIDI note number. |
| pch-&gt;freq | convert PCH format to frequency (Hz). |
| hertz | generic function for converting keyword, MIDI note number, or PCH to frequency (Hz). |

These functions are useful to allow the user to write values in a form
they find convenient and transform them into values appropriate for
music systems to process. For example, the keyword format uses Clojure
keywords to allow for note pitches to be written using the note names
and octave specifications that are common in Western art music notation.

```clojure
    user=> (keyword->notenum :C4)
    60
    user=> (keyword->notenum :Cs4)
    61
    user=> (keyword->notenum :Bb4)
    70
```

The above is an example REPL session where
keywords are used with the keyword-&gt;notenum function to
generate MIDI note numbers. The keyword :C4 describes the
note name C at octave 4, which corresponds to the MIDI note number 60
and the middle C key on a piano. Note names can be further modified by
using 's' and 'b' to denote sharps and flats.

Beyond decibels, amplitude, keywords, MIDI, and frequencies is the PCH
format. This format is described further below.

#### PCH notation

Score’s PCH notation is based on Csound [pch](http://csound.github.io/docs/manual/cpspch.html) notation. In Csound,
pch is a specially formatted number defined using "octave point pitch
class". For example, '8.01' means "octave 8, pitch class 1" and is
equivalent to the C\# above middle C on a piano. Instead of using
numbers, Score uses a 2-vector to represent PCH. The equivalent to
Csound’s '8.01' would be Score's '[8 1]'.

Besides the PCH to MIDI and frequency functions, Score provides
additional PCH-related functions.


| Function | Description | 
| -------- | ----------- |
| pch-add  |  adds an interval to a PCH and returns the new PCH, optionally taking in scale-degrees per octave (defaults to 12). |
| pch-diff | calculates the interval between two PCHs, optionally taking in scale-degrees per octave (defaults to 12). |
| pch-interval-seq | given an initial PCH, and list of intervals, generates a sequence of PCHs applying pch-add using the the previous PCH and new interval from the list. |
| analyze-intervals | given a list of PCHs, calculate the intervals between each PCH. |
| invert | create a chord inversion using a list of PCHs and inversion number. |

Note, these PCH functions take into account the number of scale degrees
per octave and normalize PCHs for overflows and underflows. For example,
when pch-add is used with [8 11], interval 1, and
scale-degrees 12, rather than return [8 12], the value will be normalised to [9 0].  The following shows an example usage of
PCH-related functions.

```clojure
    user=> (pch-add [8 0] 1)
    [8 1]
    user=> (pch-add [8 0] 13)
    [9 1]
    user=> (pch-add [8 0] -1)
    [7 11]
    user=> (pch-diff [8 0] [8 7])
    7
    user=> (pch-diff [8 0] [9 1])
    13
    user=> (pch-interval-seq [8 0] [2 3 -1])
    ([8 0] [8 2] [8 5] [8 4])
    user=> (analyze-intervals [[8 0] [8 2] [8 5]])
    [2 3]
    user=> (invert [[8 1] [8 2] [8 3]] 1)
    [[8 1] [7 2] [7 3]]
```

These functions provide useful functions for transforming PCH values and
working with intervals between PCHs. They allow for common musical
operations such as transposition and inversions. Retrogrades and
sub-list operations can be achieved using Clojure’s
reverse, drop, and take functions.

#### Tunings

score.tuning provides functions for working with musical
tunings. A tuning is defined using a Clojure map data structure with
specific key/value pairs. The following shows an
example of the twelve-tone equal temperament tuning, provided by Score.

```clojure
    (def ^:const ^{:tag 'double}
      MIDDLE-C 261.6255653005986)

    (def TWELVE-TET
      { :description "Twelve-Tone Equal Temperament"
        :base-freq MIDDLE-C
        :num-scale-degrees 12
        :octave 2.0
        :ratios (map #(Math/pow 2.0 (/ % 12)) (range 12))
       })
```

Besides defining tunings by hand, the
create-tuning-from-file function can be used to load files
in the [Scala file format](http://www.huygens-fokker.org/scala/scl_format.html). This provides access to
over 4000 scale files found in Scala’s [scale
archive](http://www.huygens-fokker.org/scala/downloads.html#scales).

Once a tuning is created, the pch-&gt;freq function found
in score.tuning can be used. This function takes in two
arguments: a tuning and a PCH. As noted earlier, PCH is a 2-element list
that provides an octave and scale degree. The result is the frequency
for a given PCH.
