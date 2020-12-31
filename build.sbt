name := "scala-arg"

organization := "com.github.dzufferey"

version := "1.0.0"

scalaVersion := "2.13.4"

crossScalaVersions := Seq("2.12.12", "2.13.4")

libraryDependencies ++=  Seq(
    "org.scalatest" %% "scalatest" % "3.2.2" % "test"
)

scalacOptions := Seq("-unchecked", "-deprecation")
