name := "Nukokusa Bot"

version := "0.1"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "org.twitter4j" % "twitter4j-stream" % "3.0.3",
    "commons-codec" % "commons-codec" % "1.7",
    "com.twitter"   % "util-eval" % "5.2.0" withSources(),
    "org.scalatest" %% "scalatest" % "1.9.1" % "test" )

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases",
                  "T repo"    at "http://maven.twttr.com/")
