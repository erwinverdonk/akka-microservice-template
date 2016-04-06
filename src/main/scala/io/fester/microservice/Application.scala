/*
 * Copyright 2016 fester.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fester.microservice

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import io.fester.microservice.api.RestService
import kamon.Kamon
import scaldi.Injectable

import scala.util._


object Application extends App with LazyLogging {
  import Injectable._

  Kamon.start()
  sys addShutdownHook {
    Kamon.shutdown()
  }

  implicit val inj = new ApplicationModule

  implicit val system: ActorSystem = inject[ActorSystem]
  import system.dispatcher

  implicit val mat = ActorMaterializer()

  val config = inject[Config]('applicationConfig)
  import com.github.kxbmap.configs.syntax._

  val cluster = Cluster(system)
  cluster.join(cluster.selfAddress)

  cluster registerOnMemberUp {
    logger.info(s"Member[${cluster.selfAddress}] is UP, starting web server")
    Http().bindAndHandle(
      handler = inject[RestService].route,
      interface = config.get[String]("server.http.interface"),
      port = config.get[Int]("server.http.port")
    ) onComplete {
      case Success(v) ⇒ logger.info(s"Successfully started http server: $v")
      case Failure(ex) ⇒
        sys.error(s"Triggering shutdown as http server could not be started: $ex")
        cluster.leave(cluster.selfAddress)
        system.terminate()
    }
  }

  cluster registerOnMemberRemoved {
    logger.warn(s"Member[${cluster.selfAddress}] has been REMOVED, terminating")
    system.registerOnTermination(System.exit(-1))
    import scala.concurrent.duration._
    system.scheduler.scheduleOnce(10.seconds)(System.exit(-1))
    system.terminate()
  }
}
