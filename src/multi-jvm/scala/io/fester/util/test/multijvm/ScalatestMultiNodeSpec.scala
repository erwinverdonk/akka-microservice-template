package io.fester.util.test.multijvm

import akka.remote.testkit.MultiNodeSpecCallbacks
import org.scalatest._


/** Hooks up scalatest with MultiNodeSpec */
trait ScalatestMultiNodeSpec
  extends MultiNodeSpecCallbacks
    with BeforeAndAfterAll {
  self: Suite ⇒

  override def beforeAll() = {
    multiNodeSpecBeforeAll()
    super.beforeAll()
  }

  override def afterAll() = {
    try
      super.afterAll()
    finally
      multiNodeSpecAfterAll()
  }
}
