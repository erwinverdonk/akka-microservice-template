package io.fester.microservice.actor.cluster

import akka.actor.{Actor, ActorLogging, Address, Props}
import akka.cluster.ClusterEvent._
import akka.cluster.MemberStatus.Down
import akka.cluster.{Cluster, Member}
import com.github.kxbmap
import com.typesafe.config.Config
import com.typesafe.config.ConfigException.BadValue


object MemberManager {
  def props(config: Config) = Props(classOf[MemberManager], config)
}


class MemberManager(config: Config) extends Actor with ActorLogging {
  implicit val cluster = Cluster(context.system)
  private val splitBrainResolver = SplitBrainResolver(config)

  private[this] var members = Set.empty[Member]
  private[this] var leader = Option.empty[Address]

  override def preStart() = cluster.subscribe(
    subscriber = self,
    initialStateMode = InitialStateAsSnapshot,
    to = classOf[MemberEvent], classOf[ReachabilityEvent], classOf[LeaderChanged])

  override def postStop() = cluster.unsubscribe(self)

  override def receive = {
    case s: CurrentClusterState ⇒
      log.debug(s">>> state=$s")
      members = s.members filterNot (_.status == Down) // todo is this the only criteria when implementing the sbr still
      leader = s.leader
    case MemberUp(m) ⇒
      log.debug(s">>> MemberUp($m)")
      members += m
    case o: MemberEvent ⇒ // ignore
    case ReachableMember(m) ⇒
      log.debug(s">>> ReachableMember($m)")
      members += m
    case UnreachableMember(m) ⇒
      log.warning(s">>> Downing UNREACHABLE member: $m")
      cluster.down(m.address)
      members -= m
    case LeaderChanged(a) ⇒
      log.debug(s">>> LeaderChanged($a)")
      leader = a
    case unhandled ⇒ sys.error(s"MemberManager received Unhandled message: $unhandled")
  }
}


object SplitBrainResolver {
  final val PolicyPath = "cluster.sbr.policy"

  import kxbmap.configs.syntax._
  def apply(config: Config)(implicit cluster: Cluster) = config.get[String](PolicyPath) match {
    case "static-quorum" ⇒ new StaticQuorumSplitBrainResolver(config)
    case x ⇒ throw new BadValue(PolicyPath, s"Unknown split-brain resolver policy: $x")
  }
}


abstract class SplitBrainResolver {
  def unreachable: Member ⇒ () ⇒ Boolean
}


class StaticQuorumSplitBrainResolver(val config: Config)(implicit cluster: Cluster) extends SplitBrainResolver {
  override val unreachable: Member ⇒ () ⇒ Boolean = m ⇒ () ⇒ true
}
