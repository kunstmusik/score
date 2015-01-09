# Csound Live Coding Example 

In the src/score/demo folder is a file called csound_demo.clj that demonstrates
using the score library with Csound via its Java API.  It requires:

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
