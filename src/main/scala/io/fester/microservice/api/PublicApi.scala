package io.fester.microservice.api

import akka.actor.ActorSystem

import scala.util.Try


/** = Public API endpoint =
  * `/api`
  *
  * @param system The [[akka.actor.ActorSystem]] to use for this endpoint.
  */
class PublicApi(implicit system: ActorSystem) {
  import akka.http.scaladsl.server.Directives._

  val route =
    extractMaterializer { implicit mat ⇒
      implicit val ec = mat.executionContext
      //@formatter:off
      pathPrefix("api") {
        pathEndOrSingleSlash {
          complete(s"Public API")
        } ~
        pathPrefix("transform") {
          path("nr-plus-one" / Segment) { nr ⇒
            complete(add_1(nr).get)
          }
        }
      }
      //@formatter:on
    }

  def add_1(nr: String) =
    Try(nr.toInt + 1) map (x ⇒ s"nr-plus-one: $x") recover { case ex ⇒ s"Failed to add one, reason=$ex" }
}
