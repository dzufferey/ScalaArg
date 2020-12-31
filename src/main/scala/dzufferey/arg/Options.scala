package dzufferey.arg

/** A default configuration class */
abstract class Options {

  private var options = List[Arg.Def]()
 
  def newOption(opt: java.lang.String, fct: Spec, doc: java.lang.String): scala.Unit = {
      options = (opt, fct, doc) :: options
  }
 
  protected var input: List[java.lang.String] = Nil

  /** process arguments that do not belong to an option (i.e. the input files). */
  def default(arg: java.lang.String): scala.Unit = {
    input = arg :: input
  }

  val usage: java.lang.String
 
  def apply(args: Seq[java.lang.String]): scala.Unit = {
    Arg.process(options, default, usage)(args)
  }

  def bashCompletion(commandName: java.lang.String): java.lang.String = {
    GenerateBashCompletion(commandName, options)
  }

}
