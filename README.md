# Utilities

This project contains a collection of utility classes that we use across multiple other projects. Since they are mostly 
utilities that do not belong to any particular domain we decided to have them separated.

In this initial version, we have started with groups:
 
```
.
└── name
    └── julatec
        └── util
            ├── algebraic
            ├── collection
            └── statistics
``` 

## Algebraic

It includes classes that implement some Algebraic Structures such as a Lattice and Interval. We will continue including 
some other useful structures as the project grow.

## Collection

It contains some data structures that are very versatile, but we have not seen in some other projects such as
Apache Common.

## Statistics

It contains some algorithms and simple data structures designed for the calculating descriptor of any given sample.
