package dzufferey.arg

object Arg {

  /** The option keyword, e.g. "-k". It must start with '-'. */
  type Key = java.lang.String

  /** a shot description of what the option does. */
  type Doc = java.lang.String

  type Def = (Key, Spec, Doc)

  private def safeCall[A,B](fct: A => B, arg: A): Option[B] = {
    try {
      Some(fct(arg))
    } catch {
      case _ : Throwable => None
    }
  }

  private def toBoolean(str: java.lang.String): Option[Boolean] = safeCall((x: java.lang.String) => x.toBoolean, str)

  private def toInt(str: java.lang.String): Option[scala.Int] = safeCall((x: java.lang.String) => x.toInt, str)

  private def toLong(str: java.lang.String): Option[scala.Long] = safeCall((x: java.lang.String) => x.toLong, str)

  private def toDouble(str: java.lang.String): Option[scala.Double] = safeCall((x: java.lang.String) => x.toDouble, str)

  private def processOption(spec: Def, args: Seq[java.lang.String]): Seq[java.lang.String] = spec._2 match {
    case Unit(fct) =>
      fct()
      args
    case Bool(fct) =>
      args.headOption match {
        case Some(arg) =>
          toBoolean(arg) match {
            case Some(b) => fct(b)
            case None => sys.error("expected boolean argument for option '"+spec._1+"' found: '" + arg + "'.")
          }
          args.tail
        case None =>
          sys.error("no (not enough) argument given for option '"+ spec._1 +"'.")
          args
      }
    case Int(fct) =>
      args.headOption match {
        case Some(arg) =>
          toInt(arg) match {
            case Some(b) => fct(b)
            case None => sys.error("expected integer argument for option '"+spec._1+"' found: '" + arg + "'.")
          }
          args.tail
        case None =>
          sys.error("no (not enough) argument given for option '"+ spec._1 +"'.")
          args
      }
    case Long(fct) =>
      args.headOption match {
        case Some(arg) =>
          toLong(arg) match {
            case Some(b) => fct(b)
            case None => sys.error("expected long integer argument for option '"+spec._1+"' found: '" + arg + "'.")
          }
          args.tail
        case None =>
          sys.error("no (not enough) argument given for option '"+ spec._1 +"'.")
          args
      }
    case Real(fct) =>
      args.headOption match {
        case Some(arg) =>
          toDouble(arg) match {
            case Some(b) => fct(b)
            case None => sys.error("expected floating point number argument for option '"+spec._1+"' found: '" + arg + "'.")
          }
          args.tail
        case None =>
          sys.error("no (not enough) argument given for option '"+ spec._1 +"'.")
          args
      }
    case String(fct) =>
      args.headOption match {
        case Some(arg) =>
          fct(arg)
          args.tail
        case None =>
          sys.error("no (not enough) argument given for option '"+ spec._1 +"'.")
          args
      }
    case Tuple(x :: xs) =>
      val rest = processOption((spec._1, x, spec._3), args)
      processOption((spec._1, Tuple(xs), spec._3), rest)
    case Tuple(Nil) =>
      args
    case Enum(enum, fct) =>
      args.headOption match {
        case Some(arg) =>
          try {
            fct(enum.withName(arg))
          } catch {
            case _: NoSuchElementException =>
              sys.error("option '" + spec._1 + "': "+ arg + " is not in "+enum.values.mkString(", "))
          }
          args.tail
        case None =>
          sys.error("no (not enough) argument given for option '"+ spec._1 +"'.")
          args
      }
  }

  private def specType(s: Spec): java.lang.String = s match {
    case Unit(_)    => ""
    case Bool(_)    => "Bool"
    case String(_)  => "String"
    case Int(_)     => "Integer"
    case Long(_)     => "Long Integer"
    case Real(_)     => "Real"
    case Tuple(lst) => lst.map(specType(_)).mkString("", " ", "")
    case Enum(enum, _) => enum.toString
  }

  private def printUsage(specs: Seq[Def], usage: java.lang.String): scala.Unit = {
    Console.println("cmd [Option(s)] file(s)")
    Console.println(usage)
    Console.println("Options:")
    for ( (opt, spec, descr) <- specs ) {
      Console.println("  " + opt + " " + specType(spec) + "  " + descr)
    }
  }

  def process(specs: Seq[Def], default: java.lang.String => scala.Unit, usage: java.lang.String)(args: Seq[java.lang.String]): scala.Unit = {
    args.headOption match { // linter:ignore UseOptionForeachNotPatMatch
      case Some(arg) =>
        if (arg == "-h" || arg == "--help") {
          printUsage(specs, usage)
          sys.exit(0)
        } else if (arg startsWith "-") {
          val args2 = specs.find( s => s._1 == arg) match {
            case Some( spec ) =>
              processOption(spec, args.tail)
            case None =>
              sys.error("unknown option '" + arg + "'.")
              args.tail
          }
          process(specs, default, usage)(args2)
        } else {
          default(arg)
          val args2 = args.tail
          process(specs, default, usage)(args2)
        }
      case None => ()
    }
  }

}

