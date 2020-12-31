package dzufferey.arg

import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite

class EnumTest extends AnyFunSuite {

  import MyBool._
  
  test("parsing enum") {
    def isTrue(b: MyBool.Value): scala.Unit = {
      assert(b == top)
    }
    def isFalse(b: MyBool.Value): scala.Unit = {
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
