package jp.nukokusabot

import org.scalatest._
import com.whalin.MemCached._

class KyotoTycoonTest extends FlatSpec {

  val kt = new MemCachedClient

  "Memcached protocol" should "restore the key and value pair" in {
    kt.set("foo", "bar",  0)
    kt.set("foo", "buzz", 1)

    assert(kt.get("foo", 0) === "bar")
    assert(kt.get("foo", 1) === "buzz")
  }

  it should "count number of atoms" in {
    kt.set("num_of_atoms", 0)

    kt.incr("num_of_atoms")
    kt.incr("num_of_atoms")
    kt.incr("num_of_atoms")

    assert(kt.get("num_of_atoms") === 3)
  }

}
