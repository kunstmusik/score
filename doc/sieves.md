# Sieves

## Introduction

"...sieve theory is the study of the internal symmetries of a series of points
either constructed intuitively, given by observation, or invented completely
from moduli of repetition." 

- Xenakis, Formalized Music, pp. 276 
  
score.sieves provides sieve functions based on the work by Iannis Xenakis. 
Sieves are represented as a 2-vector of [modulo index].  U and I are used to create Union and
Intersection Sieves. Sieves may also be nil, though are factored out when a
Sieve is simplified, and generates no sequence when gen-sieve is called.

## Usage

Sieves are used to generate a series of numbers that have been sieved according to values given by the user. The process starts with the series of positive numbers starting from zero.  The numbers are then filtered against the given sieves.  

In score.sieves, the gen-sieves function is used to generate a sequence using sieves.  The function takes in the number of steps to generate and a sieve to use.  

The basic sieve is a 2-vector of [modulo index]. The modulo gives the period of the sieve, and the gives the offset. For example, a sieve [4 1] would start at index 1, and repeat every 4:

```clojure
user=> (gen-sieve 12 [4 1])
(1 5 9 13 17 21 25 29 33 37 41 45)
```

A Union filters according to a set of sieves.  Any value that satisfies any of the sieves in the union will be passed through. This is equivalent to a logical or:

```clojure
user=> (gen-sieve 12 (U [4 1] [3 2]) )
(1 5 9 13 17 21 25 29 33 37 41 45)
```

An Intersection filters according to a set of a other sieves. However, unlike a Union, a value is passed through only if it satisfies all sieves within the intersection. This is equivalent to a logical and:

```clojure
user=> (gen-sieve 12 (I [4 1] [3 2]))
(5 17 29 41 53 65 77 89 101 113 125 137)
```

More complex sieves can be made up of nested Unions and Intersections:  

```clojure
user=> (gen-sieve 12 (U [3 2] (I [3 2] [2 0])))
(2 5 8 11 14 17 20 23 26 29 32 35)
```

When gen-sieves is called, sieves are first normalized, then simplified, before generating values.  You can view the simplified version of a sieve by using the simplified function:

```clojure
user=> (simplified (U [3 2] (I [3 2] [2 0])))
#score.sieves.Union{:l [3 2], :r [6 2]}
```

## References

## Implementations
* C Code from "Sieves" article below.
* [Haskell Music Theory](https://hackage.haskell.org/package/hmt-0.15) 
* [athenaCL](http://www.flexatone.org/athena.html) 
 
## Literature
* Xenakis and Rahn. "Sieves". Perspectives of New Music, Vol. 28, No. 1 (Winter, 1990), pp. 58-78.
* Xenakis. "Formalized Music". pp. 268-288.

