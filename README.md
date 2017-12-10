# caribou
![caribou](images/caribou.jpg)

A distributed system build with few dependencies using
[Kotlin](https://kotlinlang.org/) and [Jetty](https://www.eclipse.org/jetty/).

## Development

Run `./gradlew build` to build a jarfile.

Run `./graldew run --parallel` to start all applications.

The [requests.http](requests.http) file contains requests that
can be used in local testing with IntelliJ 2017.3 and above
(Ultimate Edition only, unfortunately).

To view the dependencies between the project jarfiles:
1. Install [Graphviz](http://www.graphviz.org/).
1. Run `./gradlew dependenciesGraph`.
1. Open `build/dependenciesGraph/graph.dot.png`.
 