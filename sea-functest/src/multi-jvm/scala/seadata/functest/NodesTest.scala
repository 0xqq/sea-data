package seadata.functest

import akka.actor.ActorSystem
import akka.remote.testconductor.RoleName
import akka.remote.testkit.{MultiNodeConfig, MultiNodeSpec}
import akka.testkit.ImplicitSender
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import seadata.broker.boot.BrokerBoot
import seadata.console.boot.ConsoleBoot
import seadata.core.Constants

object NodesTestMultiNodeConfig extends MultiNodeConfig {
  val nodeBrokerList = (1 to 5).map(i => role(s"${Constants.Roles.BROKER}$i"))

  val nodeConsoleList = (1 to 1).map(i => role(s"${Constants.Roles.CONSOLE}$i"))


  def nodeList: Seq[RoleName] = nodeBrokerList ++ nodeConsoleList

  nodeList.foreach { role =>
    val userDir = System.getProperty("user.dir")
    nodeConfig(role)(
      ConfigFactory.parseString(
        //      akka.extensions=["akka.cluster.metrics.ClusterMetricsExtension"]
        s"""|akka.cluster.metrics.native-library-extract-folder=target/native/${role.name}
            |akka.remote.artery.enabled = off
            |seadata.cluster.seeds = ["127.0.0.1:30011"]""".stripMargin)
    )
  }

  nodeBrokerList.zipWithIndex.foreach { case (role, idx) =>
    val httpPort = 30010 + (idx * 10)
    val port = 30011 + (idx * 10)
    nodeConfig(role)(
      ConfigFactory.parseString(
        //      akka.cluster.roles = [${Constants.Roles.ENGINE}]
        s"""|akka.remote.netty.tcp.port = $port
            |akka.remote.artery.canonical.port = $port
            |http.server.port = $httpPort""".stripMargin)
        .withFallback(ConfigFactory.load("sea-broker"))
    )
  }

  nodeConsoleList.zipWithIndex.foreach { case (role, idx) =>
    val httpPort = 30000 + (idx * 10)
    val port = 30001 + (idx * 10)
    nodeConfig(role)(
      ConfigFactory
        .parseString(
          s"""|akka.remote.netty.tcp.port = $port
              |akka.remote.artery.canonical.port = $port
              |http.server.port = $httpPort""".stripMargin)
        .withFallback(ConfigFactory.load("sea-console"))
    )
  }

}

abstract class NodesTest
  extends MultiNodeSpec(NodesTestMultiNodeConfig, config => ActorSystem("sea", config))
    with WordSpecLike with Matchers with BeforeAndAfterAll
    with ImplicitSender {

  import NodesTestMultiNodeConfig._

  override def beforeAll(): Unit = multiNodeSpecBeforeAll()

  override def afterAll(): Unit = multiNodeSpecAfterAll()

  override def initialParticipants: Int = roles.size

  "Sea NodesTest" should {
    "启动服务" in {
      runOn(nodeBrokerList.head) {
        new BrokerBoot(system).start()
        enterBarrier("startup")
      }
      for (role <- nodeBrokerList.tail) {
        runOn(role) {
          enterBarrier("startup")
          new   BrokerBoot(system).start()
        }
      }
      for (role <- nodeConsoleList) {
        runOn(role) {
          enterBarrier("startup")
          new ConsoleBoot(system).start()
        }
      }
    }

    "查询状态" in {
      println(s"$myself " + node(myself).address)
      java.util.concurrent.TimeUnit.SECONDS.sleep(1)
      enterBarrier("finished")
    }
  }

}


class NodesTestMultiJvmNode1 extends NodesTest

class NodesTestMultiJvmNode2 extends NodesTest

class NodesTestMultiJvmNode3 extends NodesTest

class NodesTestMultiJvmNode4 extends NodesTest

class NodesTestMultiJvmNode5 extends NodesTest

class NodesTestMultiJvmNode6 extends NodesTest
