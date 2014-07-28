package dzufferey.arg
  
sealed abstract class Spec
case class Unit(fct: () => scala.Unit) extends Spec
case class Bool(fct: scala.Boolean => scala.Unit) extends Spec
case class String(fct: java.lang.String => scala.Unit) extends Spec
case class Int(fct: scala.Int => scala.Unit) extends Spec
case class Tuple(lst: List[Spec]) extends Spec
