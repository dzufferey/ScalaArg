# ScalaArg

A scala library to process command line arguments, inspired by the Ocaml Arg module.

The library can also generate scripts for bash completion.

## Compiling

This project requires java 6 and can be build it using [sbt](http://www.scala-sbt.org/).
To install sbt follow the instructions at [http://www.scala-sbt.org/release/tutorial/Setup.html](http://www.scala-sbt.org/release/tutorial/Setup.html).

Then, in a console, execute:
```
$ sbt
> compile
```

## Using it

To use it in your projects your need to add the following two lines in your `build.sbt`:
```scala
resolvers +=  "dzufferey maven repo" at "https://github.com/dzufferey/my_mvn_repo/raw/master/repository"

libraryDependencies += "io.github.dzufferey" %% "scala-arg" % "0.1-SNAPSHOT"
```

The last line is requried if you want to use it in some other project.
If you want to use it locally do not add the `resolvers` line but instead run `sbt publishLocal`.

