package io.fester.microservice.api


/** = Main REST API endpoint =
  * `/`
  *
  * Just composes other API endpoints.
  *
  * @param publicApi Public API endpoint
  */
class RestService(publicApi: PublicApi) {
  import akka.http.scaladsl.server.Directives._

  val route =
    //@formatter:off
    pathEndOrSingleSlash {
      complete(s"Nothing to see here")
    } ~
    publicApi.route
  //@formatter:on
}
