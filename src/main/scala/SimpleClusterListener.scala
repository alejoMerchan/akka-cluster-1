import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

/**
 * Created by abelmeos on 2016/10/07.
 */
class SimpleClusterListener extends Actor with ActorLogging{

  val cluster = Cluster(context.system)

  override def preStart():Unit = {

    println("--- arrancando el proceso de suscripcion ---")
    cluster.subscribe(self,initialStateMode = InitialStateAsEvents,classOf[MemberEvent],
      classOf[UnreachableMember])


  }

  override def postStop():Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) =>
      log.info("subiendo un miembro: {}", member.address)
    case UnreachableMember(member) =>
      log.info("no se dectto el miembro")
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent => // ignore
  }

}
