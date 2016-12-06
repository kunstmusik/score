# lc! - mini-language for notation

lc! is a mini-language for generation of note lists.  It was originally written
for use in the context of live coding (hence "lc"), but it is generally useful
for note writing.  
  
lc! is inspired heavily by [ABCNotation](http://www.abcnotation.com) and
[Lilypond](http://www.lilypond.org). 


## Example

The following demonstrates three equivalent ways to notate a sequence of notes
that go from middle-C to the C one octave above.

```clojure
(lc! '(c4 d4 e4 f4 g4 a4 b4 c5))
(lc! '(c4 d e f g a b c5))
(lc! '(c4 d e f g a b c+))
```

The results of executing any of the statements above is:

```clojure
([0.0 1.0 261.6255653005986] 
 [1.0 1.0 293.6647679174076] 
 [2.0 1.0 329.6275569128699] 
 [3.0 1.0 349.2282314330039] 
 [4.0 1.0 391.99543598174927] 
 [5.0 1.0 440.0] 
 [6.0 1.0 493.8833012561241] 
 [7.0 1.0 523.2511306011972])
```

Each generated note vector has a start time, duration, and frequency value. 
By default, lc! uses a duration of 1.0 for each note, and the start time is
advanced by the duration for each note (or rest) generated. lc! also uses 
str-\>freq by default to generate frequency values for each note. Users
may supply a string conversion function as an optional second argument. Pre-made
functions are provided in the score.freq names.  For example, using 
str-\>notenum, the output would be:

```clojure
user=> (lc! '(c4 d e f g a b c5) str->notenum)
([0.0 1.0 60] 
 [1.0 1.0 62] 
 [2.0 1.0 64] 
 [3.0 1.0 65] 
 [4.0 1.0 67] 
 [5.0 1.0 69] 
 [6.0 1.0 71] 
 [7.0 1.0 72])
```

## Usage

lc! operates on a list of symbols. Symbols may be a **base duration value**, **note
symbol**, **note chord**, or **rest**. Each symbol is processed according the contents of
the symbol and the running state of the lc! process. 


### Duration Values

When a number is found, lc! updates the current base duration to the value
given.  By default, lc! uses a value of 1 as the duration for any note.  

```clojure
user=> (lc! '(c d e))
([0.0 1.0 261.6255653005986] 
 [1.0 1.0 293.6647679174076] 
 [2.0 1.0 329.6275569128699])

user=> (lc! '(4 c d e))
([0.0 4.0 261.6255653005986] 
 [4.0 4.0 293.6647679174076] 
 [8.0 4.0 329.6275569128699])
```

Notes and rests may modify the duration for the note or rest by appending : or
\> and a numeric value. The : notation uses the number following as a
multiplier. For example: 

```clojure
user=> (lc! '(c:4 d eb:2 c:4) str->notenum)
([0.0 4.0 60] 
 [4.0 1.0 62] 
 [5.0 2.0 63] 
 [7.0 4.0 60])
```

The first C4 note is generated with a duration of 4.0. The D note shows that
duration modifiers are not carried over and generated using the base duration of 1.0.
The rest of the notes show their duration modifiers applied. 

The \> notation (i.e., *carry* duration) means to make the duration until a
certain beat time.  For example:

```clojure
user=> (lc! '(c eb c>4) str->notenum)
([0.0 1.0 60] 
 [1.0 1.0 63] 
 [2.0 2.0 60])
user=> (lc! '(c d eb c>4) str->notenum)
([0.0 1.0 60] 
 [1.0 1.0 62] 
 [2.0 1.0 63] 
 [3.0 1.0 60])
```

The first example has the C note have a duration of 2.0 and the second example
has the duration of 1.0. In both examples, the notation may be read as "play C
until 4.0 beat time".  If due to the calculations the duration would be <= 0,
the note will not be generated.

Carry durations were added such that in a live coding session, a note or rest
might be used to fill out the rest of the note list. Users might then further
add notes to the list and the duration of the note would be adjusted so the
total duration of the note list would remain stable.  

### Note Symbols

Note symbols contains three parts: a note name, octave, and duration (discussed
above).  The note name is mandatory, while the other two parts are optional.  When
octave and duration are not specified, the values will be inherited from the
current state (i.e., carried over).

Note names may be C, D, E, F, G, A, or B, and they may have one additional 
modifier afterwards, either b or s, to denote flat or sharp.  The note names
may be either lower- or upper-case. Some examples:

| Note Symbol | Meaning |
| ----------- | ------- |
| C  | C at current octave |
| Cs | C# or C-sharp |
| Eb | E-flat |

Octaves are a single digit, +, or - that follows the note name.  When no octave
is given, the current octave value is inherited. If a digit is given, the value
denotes the exact octave to use.  When a + or - is used, it denotes an octave
higher or lower than the current octave. Some examples:


| Note Symbol | Meaning |
| ----------- | ------- |
| C4    | Middle-C |
| c4 d   | Middle-C, followed by D in octave 4|
| c4 d5   | Middle-C, followed by D in octave 5|
| C4 d+ | Middle-C, followed by D in octave 5|

### Note Chords

Chords may be specified by using vectors. The first symbol in the vector will describe the base properties of the note chord (base octave, duration) and any remaining symbols will only denote additional pitches.  All pitch values for the chord are generated within the pitch field (index 2) of the generated note.  For example:

```clojure
user=> (lc! '([c:2 e g] d) str->notenum)
([0.0 2.0 [60 64 67]] 
 [2.0 1.0 62])
```

The first generated note has a duration of 2.0 and has a 3-vector of MIDI note numbers denoting a C-major chord. 

### Rests

Rests are used to advance time. They are denoted using the "r" or "R" symbol
and may have an optional duration modifier attached (either multiplier or carry
duration).  They do generate output (a two-vector with just a start and
duration time). For example:

```clojure
user=> (lc! '(c r:2 eb c) str->notenum)
([0.0 1.0 60] 
 [1.0 2.0] 
 [3.0 1.0 63] 
 [4.0 1.0 60])
```

The rest value is generated above as [1.0 2.0]. User code that processes lc!-generated note lists to map to target systems will require detection and handling of rest values.

