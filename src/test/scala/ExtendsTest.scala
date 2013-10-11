package net.joe_noh.nukokusabot

import org.scalatest._

class ExtendsTest extends FlatSpec with Extends {

  "Extended List" should "multiple itself" in {
    val list = List(1,2)

    assert(list * 3 === List(1,2,1,2,1,2))
  }

}
