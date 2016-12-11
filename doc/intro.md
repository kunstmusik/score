# Introduction to Score

Score is a library of functions for creating musical scores as list data. (To note, for the purpose of this documentation, the term list is used synonymously with Clojure sequences.) It is based on the *note* as a list of values, and *scores* --- or note lists --- as a higher level organization of notes as lists of lists. Score contains useful musical functions for concepts such as pitch, scales, tunings, and amplitude.  It also contains functions for generating, transforming, and organising scores. Because it is based on standard Clojure data structures, the library is designed to interoperate well with other Clojure functions and libraries that also work with lists. Score provides numerous points of extensibility and encourages users to draw upon their Clojure skills to customise their score-writing experience to their own taste.

The design of the library is based on score functions designed and used by the author in other contexts/languages. This project also draws heavily upon:

* [Common Music](http://commonmusic.sourceforge.net/) 
* [Super Collider - Patterns Library](http://doc.sccode.org/Tutorials/Getting-Started/16-Sequencing-with-Patterns.html) 
* [CMask](http://www2.ak.tu-berlin.de/~abartetzki/CMaskMan/CMask-Manual.htm) 

## Basics
* [Design](design.md)
* [Basics of Score Writing Using Lists](basics.md)

## Musical Values
* [Musical Values](musical_values.md)
* [Beat Notation](beats.md)
* [Euclidean Rhythms](euclid.md)

## Generation
* [Score Generation](score_gen.md)
* [Mask](mask.md)
* [Sieves](sieves.md)

## Score Transformation 
* [Score Transformation](transformation.md)

## Score Organization 
* [Score Organization](score_organization.md)

## Score Mapping
* [Score Mapping](score_mapping.md)


## Examples 
* [Working with Csound](csound.md)
* [Demo Test Files](https://github.com/kunstmusik/score/tree/master/src/score/demo) 

