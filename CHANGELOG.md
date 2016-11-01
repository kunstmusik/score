# CHANGELOG for Score

All notable changes to this project will be documented in this file. This
change log follows the conventions of
[keepachangelog.com](http://keepachangelog.com/).

## [Unreleased] 

## Added

* score.lc
  * new symbol-list-based mini-language for score writing. Designed for writing
  music lines while live coding but useful for general note writing.
* score.freq 
  * made keyword-\>notenum and keyword-\>freq to also take in symbols, added
    sym-\>notenum and sym-\>freq as additional functions that call the former
  * extracted str-\>notenum function to work with strings

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
