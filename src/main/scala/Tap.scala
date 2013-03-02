package jp.nukokusabot

class Tap[A](obj: A) {
  def tap(block: A => Unit): A = {
    block(obj)
    return obj
  }
}

