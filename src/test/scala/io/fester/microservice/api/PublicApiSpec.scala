package io.fester.microservice.api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.fester.util.test.WordLikeSpec


class PublicApiSpec extends WordLikeSpec with ScalatestRouteTest {

  val publicApi = new PublicApi

  "Public API route" when {

    "called at path end or single slash" must {
      "print default message when calling path end" in {
        Get("/api") ~> publicApi.route ~> check {
          responseAs[String] should be("Public API")
        }
      }

      "print default message when called with a single slash" in {
        Get("/api/") ~> publicApi.route ~> check {
          responseAs[String] should be("Public API")
        }
      }
    }

    "called at transform/nr-plus-one" must {
      "return requested number plus one" in {
        Get("/api/transform/nr-plus-one/123") ~> publicApi.route ~> check {
          responseAs[String] should be("nr-plus-one: 124")
        }
      }

      "return an error when request can not be parsed" in {
        Get("/api/transform/nr-plus-one/123fourfivesix") ~> publicApi.route ~> check {
          responseAs[String] should be(
            """Failed to add one, reason=java.lang.NumberFormatException: For input string: "123fourfivesix"""")
        }
      }
    }
  }
}
