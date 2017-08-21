# CHANGELOG for Score

All notable changes to this project will be documented in this file. This
change log follows the conventions of
[keepachangelog.com](http://keepachangelog.com/).

## [Unreleased] 

## Added

* score.lc
  * lc! - new symbol-list-based mini-language for score writing. Designed for
    writing music lines while live coding but useful for general note list
    writing.
* score.euclid
  * implemented Euclidean Rhythm generator
* score.beats
  * functions for creating beat sequences (pats) of 1's and 0's or beat sets
  of beat indices. Work with source beat strings or hexadecimal strings.
* score.freq 
  * made keyword-\>notenum and keyword-\>freq to also take in symbols, added
    sym-\>notenum and sym-\>freq as additional functions that call the former
  * extracted str-\>notenum function to work with strings
  * added keynum as generalized function to convert value (string, symbol,
    keyword, PCH) to MIDI note number
  * added cents-\>scaler and scaler-\>cents (translated from Common Music)
* score.core
  * repeat-seq - Repeats a sequence x number of times. Returns a lazy sequence.
* score.sieves
  * added period function to Sieves protocol to calculate period of sieve
* score.ops
  * new functions for operating on score blocks: translating and scaling in 
  time, composing blocks together in series and parallel, repeating score.

## Changed

* score.core
  * convert-timed-score - in addition to single notes and note lists, now
    allows sub-timed-scores to be used.
* score.freq
  * keyword-\>notenum and related functions now use "s" to notate sharps. 
    "#" is still supported but is not guaranteed to work for keyword and symbols
    into the future (depends upon whether Clojure language changes). For example,
    now recommended to use :cs4 or :CS4 instead of :C#4. 
  * freq - updated to support strings and symbols.

## Fixed

* score.sieves
  * Intersections did not compute correctly when dealing with nil sieves, which 
    could be either provided by user or when sub-intersection produced a nil 
    sieve


## [0.3.0] - 2015-07-24

### Added

* Introduced score.sieves namespace for calculating and analyzing sequences
  using Xenakis-style sieves; please see docs/sieves.md for more information on
  usage

## [0.2.0] - 2015-03-08

* (Changelog not recorded, please see GIT commit history)

## 0.1.0 - 2014-06-08

* Initial Release 



[Unreleased]: https://github.com/kunstmusik/score/compare/0.3.0...HEAD
[0.2.0]: https://github.com/kunstmusik/score/compare/0.1.0...0.2.0
[0.3.0]: https://github.com/kunstmusik/score/compare/0.2.0...0.3.0
