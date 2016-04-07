package io.fester.microservice.actor

import akka.actor.ActorSystem
import io.fester.microservice.ApplicationModule
import io.fester.microservice.actor.cluster.MemberManager
import scaldi.Module


trait ActorModule extends Module {
  self: ApplicationModule ⇒

  import com.github.kxbmap.configs.syntax._

  bind[ActorSystem] to system destroyWith (_.terminate())

  lazy val system = ActorSystem(applicationConfig.get[String]("cluster.name"))

  system.actorOf(MemberManager.props, "memberManager")
}
