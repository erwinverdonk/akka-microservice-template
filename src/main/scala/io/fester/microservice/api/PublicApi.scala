package io.fester.microservice.api

import akka.actor.ActorSystem


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
