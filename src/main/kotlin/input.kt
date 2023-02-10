import kotlinx.browser.document
import parser.SyntaxError
import parser.lex
import parser.parseExp

fun processInput(input: String) {
    val tokens = lex(input)

    console.log(tokens.toString())

    //val parser = Parser(tokens)
    val rootNode = try {
        parseExp(tokens.toMutableList())
    } catch (e: SyntaxError) {
        null
    }

    console.log(rootNode.toString())

    document.getElementById("input-out")!!.innerHTML = rootNode?.laTeX(true)?.let { "$doubleDollar$it$doubleDollar" } ?: "invalid input"

    val simplified = rootNode?.simplyLog()
    println(simplified)

    document.getElementById("simply-out")!!.innerHTML = simplified?.laTeX(true)?.let { "$doubleDollar$it$doubleDollar" } ?: "invalid input"

    val derivative = simplified?.derivative(charArrayOf('x'))?.simplyLog()
    println(simplified?.derivative(charArrayOf('x')))
    println(derivative)

    document.getElementById("deriv-out")!!.innerHTML = derivative?.laTeX(true)?.let { "$doubleDollar$it$doubleDollar" } ?: "invalid input"
}

const val doubleDollar = "$" + "$"