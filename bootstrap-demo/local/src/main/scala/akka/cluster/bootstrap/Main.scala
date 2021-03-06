package akka.cluster.bootstrap

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.management.AkkaManagement
import akka.management.cluster.bootstrap.ClusterBootstrap
import com.typesafe.config.ConfigFactory

//#main
object Node1 extends App {
  new Main(1)
}

object Node2 extends App {
  new Main(2)
}

object Node3 extends App {
  new Main(3)
}

class Main(nr: Int) {

  val config = ConfigFactory.parseString(
    s"""
      akka.remote.artery.canonical.hostname = "127.0.0.$nr"
      akka.management.http.hostname = "127.0.0.$nr"
    """).withFallback(ConfigFactory.load())
  val system = ActorSystem("local-cluster", config)

  AkkaManagement(system).start()

  ClusterBootstrap(system).start()

  Cluster(system).registerOnMemberUp({
    system.log.info("Cluster is up!")
  })
}
//#main
