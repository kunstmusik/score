# Basics of Score Writing Using Lists 

## History 

Using lists of values as notes has a long history in computer music.  Ever since Music-N, the concept of a Score as a list of notes has been used by composers and musicians to specify events for a music processing system to render. PLF routines, introduced as early as Music IV(ftp://ccrma-ftp.stanford.edu/pub/Lisp/music-iv-programmers-manual.pdf), allowed for composers to further process note lists before rendering.  This allowed the user to write in one sets of values meaningful to them, but have those values be processed and converted into values meaningful to the computer system. Beyond static specification of lists, many note generation system--such as [CMask](http://www2.ak.tu-berlin.de/~abartetzki/CMaskMan/CMask-Manual.htm), [Score11](http://ecmc.rochester.edu/ecmc/docs/score11/), and [nGen](http://mikelkuehn.com/index.php/ng)--were created over time for the generation of note lists. It is because of the generic nature of note lists--rather than say, fixed note classes--that allowed for development of these libraries to happen externally to the system they were used with. 

Specifying and generating notes continues to be aspects of creating musical works today. Music systems such as [Csound](http://csound.github.io) or [SuperCollider](http://supercollider.github.io) may have their own concept of notes or events, as well as include facilities for score generation. Other systems, like [Common Music](http://commonmusic.sourceforge.net/), are designed as score systems or libraries in and of themselves, which may target one or many external formats to generate from their internal formats. It is worth noting that many music programs have some form of event system, whether they are text-based systems or have graphical user interfaces.  Therefore, it is the opinion of the author that working with some form of text-based event notation system is a good practice to learn, even if the ultimate goal of the user is create or use graphical event organizing and generating systems.

## Basics of Notes

Let's now look at what a note list looks like in the program Csound.  For example, let's start by looking at the following score:

```
i1 0.0 2.0 0.5 440
i1 2.0 2.0 0.5 440
i2 2.0 2.0 0.5 880
```

The score has three notes, with each note on its own line.  The notes all begin with an 'i', which the score processor in Csound understands as an i-statement which will instantiate an instrument at a given start time, for a given duration, with the following parameters.  So the first line reads: play instrument 1 at time 0.0, for duration 2.0, using 0.5 and 440 as extra arguments (i.e. for amplitude and frequency).  The second note has similar values but starts at time 2.0. The third note is run with instrument 2, at time 2.0, for duration 2.0, with 0.5 and 880.

(Now, the term *notes* is sometimes considered old fashioned, and may be miscontrued to represent a singular model of music making.  However, the term has long been used, even if the understanding is that these are really generic events that may create new instances of instruments, or may do something else entirely. Similarly, instruments in Music-N systems are sometimes criticised as being too beholden to a singular model of music, but they are really generic blocks of processing code which may be used for sound generation, event generation, or any other kind of processing.  For the purpose of this documentation, I will use the term note as it is most often the model in mind when used with event lists.  Also to note, other systems like SuperCollider have different terminology such as SynthDefs and Events, but are effectively the same as Instruments and Notes.)

For Score, which uses Clojure sequences to represent both the list of notes and the notes themselves, the above could be rewritten as:

```
[[i 1 0.0 2.0 0.5 440]
 [i 1 2.0 2.0 0.5 440]
 [i 2 2.0 2.0 0.5 880]]
```

Once we start working with lists as models for notes/events, we can draw upon all of our standard Clojure sequence generation and manipulation skills to generate and process notes. For example, rather than define the instrument that is used as part of the note, we can opt to leave it out:

```
[[0.0 2.0 0.5 440]
 [2.0 2.0 0.5 440]
 [2.0 2.0 0.5 880]]
```

That would then give us the flexibility to easily supply the instrument function, or use the same score block with multiple instruments:
```
(def score-block-0
  [[0.0 2.0 0.5 440]
   [2.0 2.0 0.5 440]
   [2.0 2.0 0.5 880]])

(def trumpet-score
  (map #(into [trumpet] %) score-block-0))

(def synth-score
  (map #(into [synth] %) score-block-0))
```

If we then printed out trumpet-score and synth-score, we would get:

```
[[trumpet 0.0 2.0 0.5 440]
 [trumpet 2.0 2.0 0.5 440]
 [trumpet 2.0 2.0 0.5 880]]

[[synth 0.0 2.0 0.5 440]
 [synth 2.0 2.0 0.5 440]
 [synth 2.0 2.0 0.5 880]]
```

The model of notes as a list of values, and scores as a list of notes, is very flexible.  We can use map, filter, reduce, and all of the standard sequence processing functions of Clojure to process the notes.  We can further use things like concat and mapcat to join together smaller blocks of notes into larger blocks.  This allows a bottom-up composing of lists that has similarities to Western Music concepts of notes, phrases, sections, and movements.  

Also to note, because these are just lists, the model of music here is backend agnostic.  The user can take a full list that represents a total score for a work, then do a final processing step to generate to a target format.  Common Music is an exemplar of this kind of design, where one can work with a single model and generate output as MIDI, Csound SCO, as well as other formats.  Score here follows in this design.

## Next Steps

The ability to specify values for notes by hand allows for a high degree of precision in notating musical events. Building upon this foundation, users should next look at processing of lists and generation of lists. While one can work by writing every note out by hand, it can be inefficient and also non-representational of the musical processes at play.  For example, if motive of music repeats itself, writing it out by hand a second time works, but using a block of notes and have it processed to start again at a later time may be more representative of "a motive is played at time x, and repeats again at time y".  Also, if an algorithm is used to generate a body of notes, it may be easier to understand and work with the algorithm itself, rather than each individual notes.   

Score contains a number of functions for aiding writing of both score generation and score processing code. From here, the user is encouraged to look through the rest of the documentation with the lens of "how does this help me process notes" and "how does this help me generate notes".  Thinking through your musical goals and considering the musical tools of specification, generation, and transformation, should hopefully provide you with an efficient and intuitive workflow for creating your music.
