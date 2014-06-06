# score 

A Clojure library for music score generation.  A note is a list of values and
a list of notes is a list of lists.

This library contains functions for various approaches to score generation. It
is made in a generic fashion so as to work with multiple approaches to 
modeling scores, as well as working with multiple backends. 

The design of the library is based on score functions designed and used by the 
author in other contexts/languages. This project also draws heavily on:

* [Common Music](http://commonmusic.sourceforge.net/) 
* [Super Collider - Patterns Library](http://doc.sccode.org/Tutorials/Getting-Started/16-Sequencing-with-Patterns.html) 
* [CMask](http://www2.ak.tu-berlin.de/~abartetzki/CMaskMan/CMask-Manual.htm) 

(More information to come...)

## Usage

Documentation to follow.


## Note

This library is currently being developed for use with Csound. The design of
the library however is agnostic to backend.  The library may be expanded for 
use with MIDI, OSC, and other protocols/systems.  Also, in the first phase of
development, score generation occurs ahead-of-time.  Future iterations will
add appropriate utility functions for generation to occur in real-time. 

## License

Copyright © 2014 Steven Yi 

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
