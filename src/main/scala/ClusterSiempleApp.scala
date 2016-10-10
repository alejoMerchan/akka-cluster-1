import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Created by abelmeos on 2016/10/07.
 */
object ClusterSiempleApp {

  def  main (args: Array[String]){

    if (args.isEmpty)
      startUp(Seq("2551", "2552","2553", "0"))
    else
      startUp(args)

  }

  def startUp(ports:Seq[String]):Unit = {

    ports foreach{
      port =>
        val confg = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())

        // Create an Akka system
        val system = ActorSystem("ClusterSystem", confg)
        // Create an actor that handles cluster domain events
        system.actorOf(Props[SimpleClusterListener], name = "clusterListener")

    }

  }


}
