package dzufferey.arg

import org.scalatest._

object MyBool extends Enumeration {
  val top, bot = Value
}

class EnumTest extends FunSuite {

  import MyBool._
  
  test("parsing enum") {
    def isTrue(b: MyBool.Value) {
      assert(b == top)
    }
    def isFalse(b: MyBool.Value) {
      assert(b == bot)
    }
    val d = List(
      ("-t", Enum(MyBool, isTrue), ""),
      ("-b", Enum(MyBool, isFalse), "")
    )
    Arg.process(d, _ => (), "...")(Seq("-t", "top"))
    Arg.process(d, _ => (), "...")(Seq("-b", "bot"))
    intercept[java.lang.RuntimeException] {
        Arg.process(d, _ => (), "...")(Seq("-t", "bot"))
    }
    intercept[java.lang.RuntimeException] {
        Arg.process(d, _ => (), "...")(Seq("-b", "top"))
    }
    intercept[java.lang.RuntimeException] {
        Arg.process(d, _ => (), "...")(Seq("-b", "asdf"))
    }
  }

}
