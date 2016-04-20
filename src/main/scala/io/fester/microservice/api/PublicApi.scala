package io.fester.microservice.api

import akka.actor.ActorSystem



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
      pathPrefix("api") {
        complete(s"Public API")
      }
    }
}
