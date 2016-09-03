# Introduction to Score

Score is a library of functions for creating musical scores as list data. (To note, for the purpose of this documentation, the term list is used synonymously with Clojure sequences.) It is based on the note as a list of values, and higher level organization of notes as lists of lists. It also contains useful value convertors for expressing things like pitch, scales, tunings, and amplitude.  Because it is based on standard Clojure data structures, the library is designed to offer tools to the user without restricting the user to those tools.  The user is encouraged to draw upon their standard Clojure skills to customize their score-writing experience to their own way of looking at and working with musical scores.

The design of the library is based on score functions designed and used by the author in other contexts/languages. This project also draws heavily on:

* [Common Music](http://commonmusic.sourceforge.net/) 
* [Super Collider - Patterns Library](http://doc.sccode.org/Tutorials/Getting-Started/16-Sequencing-with-Patterns.html) 
* [CMask](http://www2.ak.tu-berlin.de/~abartetzki/CMaskMan/CMask-Manual.htm) 

## Basics
* [Basics of Score Writing Using Lists](basics.md)

## Values
* [Pitch and Scales](pitch.md)
* [Amplitude](amplitude.md)

## Explicit Scoring
* [Measured and Timed Scores](measured_score.md)

## Score Manipulation
* [Score Manipulation](manipulation.md)

## Generation
* [Score Generation](score_gen.md)
* [Mask](mask.md)
* [Sieves](sieves.md)

## Examples 
* [Working with Csound](csound.md)
* [Demo Test Files](https://github.com/kunstmusik/score/tree/master/src/score/demo) 

