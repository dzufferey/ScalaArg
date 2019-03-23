package dzufferey.arg

import org.scalatest._

class BashCompletionTest extends FunSuite {

  import MyBool._
  
  test("1") {
    val d = List(
      ("-u", Unit(() => ()), ""),
      ("-b", Bool(_ => ()), ""),
      ("-i", Int(_ => ()), ""),
      ("-t", Enum(MyBool, (_: MyBool.Value) => ()), "")
    )
    val result = GenerateBashCompletion("testing", d)
    val expected = """_testing() 
{
    local cur prev words cword split
    _init_completion -s || return


    case "${prev}" in
        -b)
            COMPREPLY=( $(compgen -W "true false" -- ${cur}) )
            return
            ;;
        -i)
            COMPREPLY+=( $( compgen -P "$cur" -W "{0..9}" ) )
            compopt -o nospace
            return
            ;;
        -t)
            COMPREPLY=( $(compgen -W "top bot" -- ${cur}) )
            return
            ;;
    esac

    case "${cur}" in
        -*)
            COMPREPLY=( $(compgen -W "-u -b -i -t" -- ${cur}) )
            return 0
            ;;
        *)
            _filedir
            return
            ;;
    esac
}
complete -F _testing testing || echo error
"""
    assert(result == expected)
  }

}
