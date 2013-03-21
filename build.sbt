name := "Nukokusa Bot"

version := "0.1"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "org.twitter4j" % "twitter4j-core" % "3.0.3",
    "org.twitter4j" % "twitter4j-stream" % "3.0.3",
    "commons-codec" % "commons-codec" % "1.7",
    "com.whalin" % "Memcached-Java-Client" % "3.0.1",
    "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
    "org.slf4j" % "slf4j-api" % "1.7.2",
    "ch.qos.logback" % "logback-core" % "1.0.9",
    "ch.qos.logback" % "logback-classic" % "1.0.9",
    "org.scala-lang" % "scala-actors" % "2.10.0",
    "org.scalatest" %% "scalatest" % "1.9.1" % "test" )

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases",
                  "T repo"    at "http://maven.twttr.com/")
