package ejemplo1

import akka.actor.{RootActorPath, Actor}
import akka.cluster.{MemberStatus, Member, Cluster}
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberUp}

/**
 * Created by abelmeos on 2016/10/07.
 */
class TransformationBackend extends Actor{

  val cluster = Cluster(context.system)

  /**
   * registra el actor en el cluster como un MemberUp
   */
  override def preStart(): Unit = {

    cluster.subscribe(self, classOf[MemberUp])
    println("--- crea un transformation backend y lo suscribe al cluster")

  }
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case TransformationJob(text) => sender() ! TransformationResult(text.toUpperCase)
    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach register
    case MemberUp(m) => register(m)
  }

  def register(member: Member): Unit =
    if (member.hasRole("frontend"))
      context.actorSelection(RootActorPath(member.address) / "user" / "frontend") ! BackendRegistration

}
