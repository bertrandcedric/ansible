javaOptions in run += "-Xmx8192M -XX:MaxPermSize=256M"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.pakulov.kafka" % "kafka-graphite-clients" % "0.3.0",
  "com.pakulov.kafka" % "kafka_2.10-graphite" % "0.3.0"
)

test in assembly := {}
