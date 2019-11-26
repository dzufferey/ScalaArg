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

}

//  /** sample configuration object */
//  object Options extends Options {
//   
//    //verbosity
//    newOption("-v", Arg.Unit(() => Logger.moreVerbose), "increase the verbosity level.")
//    newOption("-q", Arg.Unit(() => Logger.lessVerbose), "decrease the verbosity level.")
//    newOption("--hide", Arg.String( str => Logger.disallow(str)), "hide the output with given prefix.")
//    newOption("--noAssert", Arg.Unit(() => Logger.disableAssert), "remove some assertions.")
//   
//    //general reporting option
//    var report = false
//    var reportOutput: Option[String] = None
//    var stats = false
//   
//    newOption("-r", Arg.Unit(() => report = true), "output a report (with a default name).")
//    newOption("--report", Arg.String(str => { report = true; reportOutput = Some(str) } ), "output a report with given name.")
//    newOption("--stats", Arg.Unit(() => stats = true), "print statistics about the execution.")
//   
//    //general config stuff
//    //var maxChildren = -1
//    //newOption("--maxChildren", Arg.Int ( i => maxChildren = i), "limit the number of children that can be spawned at the same time (default: no limit).")
//   
//    newOption("--smtSolver", Arg.String(str => smtlib.Solver.setCmd(str.split(" "))), "The smt sovler (+ options) to use (default: \"z3 -smt2 -in\").")
//   
//    val usage = "..."
//  
//  }
