package jp.nukokusabot

trait Extends {
  class ListWrapper[A](self: List[A]) {

    def *[B](n: Int): List[A] = {
      def appendOneself(m: Int, acc: List[A]): List[A] = m match {
        case i if i > 1 => appendOneself(m-1, acc.:::[A](self))
        case _ => acc
      }
      appendOneself(n, self)
    }
  }

  implicit def list2listWrapper[A](list: List[A]) = new ListWrapper[A](list)
}
