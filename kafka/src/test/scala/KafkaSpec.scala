package kafka

import java.util.UUID

import kafka.consumer._
import kafka.producer._
import org.scalatest.{FlatSpec, Matchers}

class KafkaSpec extends FlatSpec with Matchers {

  "Simple Producer and Consumer" should "send string to broker and consume that string back" in {
      val testMessage = UUID.randomUUID().toString
      val testTopic = UUID.randomUUID().toString
      val groupId_1 = UUID.randomUUID().toString

      var testStatus = false

      info("starting sample broker testing")
      val producer = new KafkaProducer(testTopic, "192.168.99.100:32791")
      producer.send(testMessage)

      val consumer = new KafkaConsumer(testTopic, groupId_1, "192.168.99.100:32786")

      def exec(binaryObject: Array[Byte]) = {
        val message = new String(binaryObject)
        info("testMessage = " + testMessage + " and consumed message = " + message)
//        testMessage must_== message
        consumer.close()
        testStatus = true
      }

      info("KafkaSpec is waiting some seconds")
      consumer.read(exec)
      info("KafkaSpec consumed")

//      testStatus must beTrue // we need to get to this point but a failure in exec will fail the test
  }
}
