name := "grs-train-api"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Cloudera Repository" at "https://repository.cloudera.com/artifactory/repo/"
resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.2-1004-jdbc4",
  "org.http4s" %% "http4s-dsl" % "0.6.5",
  "org.http4s" %% "http4s-blazeserver" % "0.6.5",
  "org.http4s" %% "http4s-argonaut" % "0.6.5",
  "org.slf4j" % "slf4j-api" % "1.7.7" ,
  "org.slf4j" % "slf4j-log4j12" % "1.7.7",
  "org.apache.hbase" % "hbase-client" % "0.96.1.1-cdh5.0.2" ,
  "org.apache.hbase" % "hbase-common" % "0.96.1.1-cdh5.0.2" ,
  "org.apache.hadoop" % "hadoop-client" % "2.3.0-cdh5.0.2",
  "org.json4s" %% "json4s-native" % "3.2.11"
)
