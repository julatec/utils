# Bloxplot in console

We provide a very simple example of how to create boxplot 
in console. We provide two classes, the first one is [RandomGroupGenerator.java](RandomGroupGenerator.java), 
and the second one is [BoxPlotTee.java](BoxPlotTee.java). To run the example:

## How to run

### Compile test classes

```shell script
mvn test-compile
```

### Export CLASSPATH

```shell script
export CLASSPATH=target/classes/:target/test-classes/
export COLUMNS
```

### Run the random number generator

```shell script
java name.julatec.util.statistics.RandomGroupGenerator
G001                   57
G002                   39
G000                   48
G002                   -5
G005                   83
G002                    3

```

### Get the boxplot

```shell script
clear
java name.julatec.util.statistics.RandomGroupGenerator | java name.julatec.util.statistics.BoxPlotTee
```

The result is similar to this:

```
Groups   -181                                                              174
     G000              |-----------[===+=====]---------------|                  
     G001 |[========================+=======================]--------------|    
     G002                           |--[===+=]--|                               
     G003                |-------------[=====+===========]----------|           
     G004       |-----------------[===+==============]---|                      
     G005        |[=====================+============]----------|               
     G006                    |---[==+=======]-------|                           
     G007                        |----[=+===]-------|                           
     G008                                |[+]|                                  
          |--------------------------[====+=====]--------------------------|    
Groups   -181                                                              174
G000                   43
G002                    6
G007                    9
G005                  -75
G006                  -54
G003                   14
G007                  -19

```


### 