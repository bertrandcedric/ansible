javaOptions in run += "-Xmx8192M -XX:MaxPermSize=256M"

//scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.12" % "test",
  "org.apache.kafka" % "kafka_2.11" % "0.9.0.1",
  "org.apache.kafka" % "connect-api" % "0.9.0.1",
  "org.apache.kafka" % "connect-file" % "0.9.0.1",
  "com.twitter" % "hbc-core" % "2.2.0",
  "org.json" % "json" % "20160212",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

test in assembly := {}
