# Euclidean Rhythms

Euclidean rhythms were discovered by Godried Toussaint and explored in his paper ["The Euclidean Algorithm Generates Traditional Musical Rhythms"](http://cgm.cs.mcgill.ca/~godfried/publications/banff.pdf). The euclid function, found in the score.euclid namespace, provides a simple way to generate rhythmic patterns using the number of hits, total number of steps, and optional rotation argument.  Using the number of hits and total number of steps, the algorithm evenly distributes the number of hits within the number of steps. For example:

```clojure
user=> (euclid 9 16)
(1 0 1 1 0 1 0 1 0 1 1 0 1 0 1 0)
```

The euclid function returns a sequence of 1's and 0's, where 1's represent a hit and 0's represent a silence; the length of the sequence is equal to the steps argument provided to the function.  

The euclid function's optional rotation argument will cause the sequence to start x number of steps from the beginning and wrap around.  For example:

```clojure
user=> (euclid 9 16 2)
(1 1 0 1 0 1 0 1 1 0 1 0 1 0 1 0)
```
The result of the above is equivalent to the first example with the exception that the sequence now starts at step 2 (where steps are 0-indexed). 

Toussaint's paper provides further details about the algorithm and how various combinations of hits, steps, and rotations can be used to generate rhythms commonly found in many musical traditions from around the world.  
