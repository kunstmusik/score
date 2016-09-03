# Mask

Score includes a library of functions based on Andre Bartetzki's [CMask](http://www2.ak.tu-berlin.de/~abartetzki/CMaskMan/CMask-Manual.htm), a well-known library for generating scores for Csound.  However, Score's Mask library functions generate lists that are not specific to Csound.

Unlike the standard use of sequences, Mask generator and processing functions are passed the current start time of the event being generated. Having this contextual information allows for generators and processors to vary over time.

All known features of CMask have been implemented in Score's Mask system, as well as some useful combinator functions.  


