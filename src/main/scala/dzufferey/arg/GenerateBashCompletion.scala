package dzufferey.arg

import java.io._

object GenerateBashCompletion {

  import Arg.Def

  def header(cmd: java.lang.String, printer: BufferedWriter): scala.Unit = {
    printer.write("_")
    printer.write(cmd)
    printer.write("""()
{
    local cur prev words cword split
    _init_completion -s || return

""")
  }

  def canCompleteArg(d: Def) = d._2 match {
    case Bool(_) | Int(_) | Real(_) | Enum(_, _) => true
    case _ => false
  }

  def completeOptionArg(specs: Seq[Def], printer: BufferedWriter): scala.Unit = {
    val s2 = specs.filter(canCompleteArg)
    if (s2.nonEmpty) {
      printer.newLine()
      printer.write("    case \"${prev}\" in")
      printer.newLine()
      s2.foreach{ case (name, spec, _) =>
        printer.write("        " + name + ")")
        printer.newLine()
        spec match {
          case Bool(_) =>
            printer.write("""            COMPREPLY=( $(compgen -W "true false" -- ${cur}) )
            return
            ;;""")
            printer.newLine()
          case Int(_) | Long(_) | Real(_) =>
            //TODO improve: negative number, decimal point, ...
            printer.write("""            COMPREPLY+=( $( compgen -P "$cur" -W "{0..9}" ) )
            compopt -o nospace
            return
            ;;""")
            printer.newLine()
          case Enum(enum, _) =>
            val cases = enum.values.mkString(" ")
            printer.write("            COMPREPLY=( $(compgen -W \"" + cases + """" -- ${cur}) )
            return
            ;;""")
            printer.newLine()
          case other =>
            sys.error("unexpected " + other)
        }
      }
      printer.write("    esac")
      printer.newLine()
    }
  }
  
  def completeOption(specs: Seq[Def], printer: BufferedWriter): scala.Unit = {
    if (specs.nonEmpty) {
      val (minus, tmp1) = specs.partition{ case (n, _, _) => n startsWith "-" }
      val (plus, rest) = tmp1.partition{ case (n, _, _) => n startsWith "+" }
      printer.newLine()
      printer.write("    case \"${cur}\" in")
      printer.newLine()
      if (minus.nonEmpty) {
        val opts = minus.map(_._1).mkString(" ")
        printer.write("""        -*)
            COMPREPLY=( $(compgen -W """" + opts + """" -- ${cur}) )
            return 0
            ;;""")
        printer.newLine()
      }
      if (plus.nonEmpty) {
        val opts = plus.map(_._1).mkString(" ")
        printer.write("""        +*)
            COMPREPLY=( $(compgen -W "${""" + opts + """}" -- ${cur}) )
            return 0
            ;;""")
        printer.newLine()
      }
      if (rest.nonEmpty) {
        val opts = rest.map(_._1).mkString(" ")
        printer.write("""        *)
            COMPREPLY=( $(compgen -W "${""" + opts + """}" -- ${cur}) )
            [[ $COMPREPLY ]] || _filedir
            return
            ;;""")
        printer.newLine()
      } else {
        printer.write("""        *)
            _filedir
            return
            ;;""")
        printer.newLine()
      }
      printer.write("    esac")
      printer.newLine()
    }
  }

  def footer(cmd: java.lang.String, printer: BufferedWriter): scala.Unit = {
    printer.write("}")
    printer.newLine()
    printer.write("complete -F _" + cmd + " " + cmd + " || echo error")
    printer.newLine()
  }
  
  def apply(cmd: java.lang.String, specs: Seq[Def], writer: BufferedWriter): scala.Unit = {
    header(cmd, writer)
    completeOptionArg(specs, writer)
    completeOption(specs, writer)
    footer(cmd, writer)
  }

  def apply(cmd: java.lang.String, specs: Seq[Def]): java.lang.String = {
    val swriter = new StringWriter()
    val bwriter = new BufferedWriter(swriter)
    apply(cmd, specs, bwriter)
    bwriter.close
    swriter.close
    swriter.toString
  }

}
