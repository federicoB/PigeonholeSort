# Pigeonhole sort + visualization

## Installation
Clone the project:
```
git clone https://github.com/federicoB/PigeonholeSort
```

To compile the project you must have [gradle](https://gradle.org/install/) installed in your system.
Execute the command to build:
```
gradle jfxjar
```
In the main directory it will be created the .jar and the ```lib``` with the dependencies.


### Project structure

The project has 2 packages:
* algorithm: contains the abstract PigeonholeSort class that provides polymorphic methods for sorting.
* visualization: set of classes to create the javaFx application that displays the execution of the algorithm.

The pigeonholeSort class is not used directly in visualization but the sorting method is the same.

## License
This project is licensed under the terms of the GNU General Public License v3.