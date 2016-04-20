package io.fester.util.test

import org.scalactic.TimesOnInt
import org.scalatest._


trait WordLikeSpec
  extends WordSpecLike
    with Matchers
    with OptionValues
    with Inside
    with Inspectors
    with GivenWhenThen
    with PrivateMethodTester
    with TimesOnInt {
}
