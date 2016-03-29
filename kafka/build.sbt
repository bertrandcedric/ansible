javaOptions in run += "-Xmx8192M -XX:MaxPermSize=256M"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "0.9.0.1"

libraryDependencies += "com.twitter" % "hbc-core" % "2.2.0"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"
