# scala-arg

![Version 1.0.0](https://img.shields.io/badge/version-1.0.0-green.svg)

A scala library to process command line arguments, inspired by the Ocaml Arg module.

The library can also generate scripts for bash completion.

## Using it

To use it in your projects your need to add the following two lines in your `build.sbt`:
```scala
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies += "com.github.dzufferey" %% "scala-arg" % "1.0.0"
```

The last line is requried if you want to use it in some other project.
If you want to use it locally do not add the `resolvers` line but instead run `sbt publishLocal`.

## Example

```scala
import dzufferey.arg._
/* A sample configuration object.
 * The object needs to extend the Options class.
 */
object ExampleOptions extends Options {

  // newOption takes 3 arguments as parameter
  // - a string for the option
  // - specifying the type of the argument and a function to apply of the option occurs
  // - an explanation of the option

  newOption("-v", Arg.Unit(() => Logger.moreVerbose), "increase the verbosity level.")
  newOption("-q", Arg.Unit(() => Logger.lessVerbose), "decrease the verbosity level.")
  newOption("--hide", Arg.String( str => Logger.disallow(str)), "hide the output with given prefix.")

  var report = false
  var stats = false

  newOption("-r", Arg.Unit(() => report = true), "output a report (with a default name).")
  newOption("--stats", Arg.Bool( b => stats = b), "print statistics about the execution.")

  var maxChildren = -1
  newOption("--maxChildren", Arg.Int ( i => maxChildren = i), "limit the number of children that can be spawned at the same time (default: no limit).")

  val usage = "..."

}
```

## Bash Completion

You can call `bashCompletion` with the name of the name of the command using the `Options` sub-class.
This method return a string that you can add to your `~/.bashrc`.

## Compiling

This project requires java 6 and can be build it using [sbt](http://www.scala-sbt.org/).
To install sbt follow the instructions at [http://www.scala-sbt.org/release/tutorial/Setup.html](http://www.scala-sbt.org/release/tutorial/Setup.html).

Then, in a console, execute:
```
$ sbt
> compile
```

