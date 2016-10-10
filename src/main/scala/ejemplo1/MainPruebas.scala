package ejemplo1

import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by abelmeos on 2016/10/07.
 */
object MainPruebas{


   def main (args: Array[String]):Unit ={


     val port = if(args.isEmpty) "0" else "2552"

     val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
       withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]")).
       withFallback(ConfigFactory.load())

     val system = ActorSystem("ClusterSystem", config)
     val frontend = system.actorOf(Props[TransformationFrontend], name = "frontend")

     implicit val timeout = Timeout(5 seconds)

     (frontend ? TransformationJob("prueba 1")) onSuccess{

       case result => println(result)

     }


  }

}
