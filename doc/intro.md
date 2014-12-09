# Introduction to Score

Score is a library of functions for creating musical scores as list data. It is based on the note as a list of values, and higher level organization of notes as lists of lists. It also contains useful value convertors for expressing things like pitch, scales, tunings, and amplitude.  Because it is based on standard Clojure data structures, the library is designed to offer tools to the user without restricting the user to those tools.  The user is encouraged to draw upon their standard clojure skills to customize their score-writing experience to their own way of looking at and working with musical scores.

## Values
* [Pitch and Scales](pitch.md)
* [Amplitude](amplitude.md)

## Explicit Scoring
* [Measured Scores](measured_score.md)

## Generation
* [Score Generation](score_gen.md)
* [Mask](mask.md)

## Score Manipulation
* [Score Manipulation](manipulation.md)


====

(The following is older information and will be reorganized...)

The design of the library is based on score functions designed and used by the 
author in other contexts/languages. This project also draws heavily on:

* [Common Music](http://commonmusic.sourceforge.net/) 
* [Super Collider - Patterns Library](http://doc.sccode.org/Tutorials/Getting-Started/16-Sequencing-with-Patterns.html) 
* [CMask](http://www2.ak.tu-berlin.de/~abartetzki/CMaskMan/CMask-Manual.htm) 

The library currently offers two styles of score generation. One is styled
after SuperCollider's Patterns. Patterns in SC generate values without context,
and map directly to standard Clojure sequences. gen-notes and gen-score in
src/score/core.clj are functions for use with the score generation style. With
this it is simple enough to emulate any feature in SC Patterns using standard
Clojure sequence-related functions.

The other score generation style is CMask-based. In CMask, rather than have
sequences, generator functions are used that function within a context of time.
(The start time of the current event being generated is passed-in as an
argument.) That difference of having time as an argument allows to express
things like time-varying masks, frequencies, etc. So far, I have completed
porting all of the features of CMask and have done light testing.

As for the future of this library, I will be using this in my pieces moving
forward, and expect to maintain this library, adding features as required. I
would warn that the library is still a little volatile, so functions may move
namespaces and users may need to update code between these early versions. I
hope to clean up and stabilize the API soon so backwards compatibility can be
maintained. (The library is version 0.1.0 at the moment; it will be bumped to
1.0.0 when the API is stable.)

Also to note, the library is purposely designed to be generic. I am targeting
Csound score generation at the moment, but the core of the library works to
generate simply lists of lists (see core.clj, and note the difference between
gen-notes and gen-score, or gen-notes2 and gen-score2). This allows the library
to be used beyond Csound. For example, you could always create a formatting
function to send the notes as MIDI, OSC, etc. (I have some plans to do some
interesting event exploration using score with a Clojure music system I'm
working on.)

For examples, I have some demo clj files I used while developing within a REPL.
[They](https://github.com/kunstmusik/score/tree/master/src/score/demo) show a
bit of what using the library would look like.

## Csound Live Coding Example 

In the src/score/demo folder is a file called csound_demo.clj that demonstrates
using the score library with Csound.  It requires:

* [Leinginen](http://leiningen.org)
* [Csound](http://csound.github.io)

To run the example:

* Open a terminal and change directories to the root of this project
* Type ```lein repl``` to start a REPL
* Open src/score/demo/csound_demo.clj within a text editor that has support
for using a REPL (i.e. vim with vim-fireplace)
* Evaluate the file (with vim-fireplace, the command is ```:Require!```)
* Go to the (comment) section. There are three sections of code.  The first
will create an instance of Csound and start it using the orc code defined
earlier in the file.  The second code uses the score library to generate
Csound SCO text, then sends it to the running Csound instance using 
.InputMessage on the CsoundPerformanceThread.  The final code will turn off
Csound.
