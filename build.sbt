name := "Nukokusa Bot"

version := "0.1"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "org.twitter4j" % "twitter4j-stream" % "3.0.3",
    "org.specs2" %% "specs2" % "latest.integration" % "test" )

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")
