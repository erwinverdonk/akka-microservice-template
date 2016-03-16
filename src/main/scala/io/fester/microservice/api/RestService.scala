package io.fester.microservice.api


class RestService(publicApi: PublicApi) {
  import akka.http.scaladsl.server.Directives._

  val route =
    pathEndOrSingleSlash {
      complete(s"Nothing to see here")
    } ~
    publicApi.route
}
