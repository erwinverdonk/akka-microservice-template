package io.fester.microservice.actor.cluster

import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.remote.testkit.{MultiNodeConfig, MultiNodeSpec}
import akka.testkit.ImplicitSender
import com.typesafe.config.ConfigFactory
import io.fester.util.test.WordSpecLikeUnitSpec
import io.fester.util.test.multijvm.ScalatestMultiNodeSpec


class MemberManagerSpecMultiJvmNode1 extends MemberManagerSpec
class MemberManagerSpecMultiJvmNode2 extends MemberManagerSpec
class MemberManagerSpecMultiJvmNode3 extends MemberManagerSpec

object MemberManagerSpec {
  object Config extends MultiNodeConfig {
    debugConfig(on = true)
    testTransport(on = true)
    val first = role("first")
    val second = role("second")
    val third = role("third")

    val nodeList = Seq(first, second, third)

    // extract individual sigar library for each node
    nodeList foreach { role ⇒
      nodeConfig(role) {
        ConfigFactory.parseString(
          s"""
             |akka.cluster.metrics.enabled = off
             |akka.extensions = ["akka.cluster.metrics.ClusterMetricsExtension"]
             |akka.cluster.metrics.native-library-extract-folder=native/${role.name}
           """.stripMargin)
      }
    }

    // this configuration will be used for all nodes
    commonConfig(ConfigFactory.parseString(
      s"""
         |akka.loggers = ["akka.event.slf4j.Slf4jLogger"]
         |akka.loglevel = "DEBUG"
         |akka.logging-filter = "akka.event.slf4j.Slf4jLoggingFilter"
         |akka.actor.provider = "akka.cluster.ClusterActorRefProvider"
         |akka.remote.log-remote-lifecycle-events = off
         |akka.cluster.allow-weakly-up-members = on
         |akka.cluster.min-nr-of-members = 2
         |akka.cluster.use-dispatcher = cluster-dispatcher
         |cluster-dispatcher {
         |  type = "Dispatcher"
         |  executor = "fork-join-executor"
         |  fork-join-executor {
         |    parallelism-min = 2
         |    parallelism-max = 4
         |  }
         |}
       """.stripMargin))
  }
}


class MemberManagerSpec
  extends MultiNodeSpec(MemberManagerSpec.Config)
    with WordSpecLikeUnitSpec
    with ScalatestMultiNodeSpec
    with ImplicitSender {

  import MemberManagerSpec.Config._

  import scala.concurrent.duration._

  override def initialParticipants = roles.size

  "illustrate how to startup cluster" in within(15 seconds) {
    val cluster = Cluster(system)

    system.actorOf(MemberManager.props(ConfigFactory.empty()), "member-manager")
    testConductor.enter("member-managers-started")

    cluster.subscribe(testActor, classOf[MemberUp])
    expectMsgClass(classOf[CurrentClusterState])

    val firstAddress = node(first).address
    val secondAddress = node(second).address
    val thirdAddress = node(third).address

    cluster join firstAddress

    (receiveN(3) collect { case MemberUp(m) ⇒ m.address } toSet) shouldBe Set(firstAddress, secondAddress, thirdAddress)

//    runOn(second) {
//      import system.dispatcher
//      println(s"shutting down connection between first and second nodes")
//      testConductor.abort(first, second) onComplete (t ⇒ println)
//
//      println(s"waiting for second node to be available")
//      awaitCond(Cluster(system).failureDetector.isAvailable(secondAddress),
//        message = s"timed out while waiting for $secondAddress")
//    }

    println(s"un-subscribing")
    cluster.unsubscribe(testActor)

    testConductor.enter("all-up")
  }
}
