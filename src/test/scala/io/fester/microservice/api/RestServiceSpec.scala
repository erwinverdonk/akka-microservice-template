package io.fester.microservice.api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.fester.util.test.WordLikeSpec


class RestServiceSpec extends WordLikeSpec with ScalatestRouteTest {

  val restService = new RestService(new PublicApi)

  "Rest service route" when {

    "called at path end or single slash" must {
      "print default message when called at path end" in {
        Get() ~> restService.route ~> check {
          responseAs[String] should be("Nothing to see here")
        }
      }

      "print default message when called with a single slash" in {
        Get("/") ~> restService.route ~> check {
          responseAs[String] should be("Nothing to see here")
        }
      }
    }
  }
}
