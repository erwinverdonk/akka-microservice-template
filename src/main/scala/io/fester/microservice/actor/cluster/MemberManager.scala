package io.fester.microservice.actor.cluster

import akka.actor.{Actor, ActorLogging, Address, Props}
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, Member}


object MemberManager {
  def props = Props(classOf[MemberManager])
}


class MemberManager extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

  private[this] var members = Set.empty[Member]
  private[this] var leader = Option.empty[Address]

  override def preStart() = cluster.subscribe(
    subscriber = self,
    initialStateMode = InitialStateAsEvents,
    to = classOf[MemberEvent], classOf[ReachabilityEvent], classOf[LeaderChanged])

  override def postStop() = cluster.unsubscribe(self)

  override def receive = {
    case MemberUp(m) ⇒ members += m
    case o: MemberEvent ⇒ // ignore
    case ReachableMember(m) ⇒ members += m
    case UnreachableMember(m) ⇒
      log.warning(s"Downing UNREACHABLE member: $m")
      cluster.down(m.address)
      members -= m
    case LeaderChanged(a) ⇒ leader = a
    case unhandled ⇒ sys.error(s"MemberManager received Unhandled message: $unhandled")
  }
}
