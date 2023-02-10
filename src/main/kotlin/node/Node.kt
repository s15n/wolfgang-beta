package node

interface Node {
    fun derivative(vars: CharArray): Node

    fun simplyLog(): Node {
        return if (logSimply) simplyLog(this, ++indentX) else simply()
    }

    fun simply(): Node

    fun laTeX(first: Boolean): String

    fun isLaTeXInline(): Boolean {
        return false
    }

    override fun toString(): String

    operator fun plus(n: Node) = Sum(this, n)
    operator fun minus(n: Node) = Diff(this, n)
    operator fun times(n: Node) = Prod(this, n)
    operator fun div(n: Node) = Div(this, n)
    operator fun unaryMinus(): Node = Neg(this)
    fun pow(n: Node) = Power(this, n)
    fun inv(): Node = Inv(this)
}

fun laTeXFunction(op: String, x: Node): String {
    val brackets = !x.isLaTeXInline()
    return "$op${if (brackets) "\\left(" else " "}${x.laTeX(true)}${if (brackets) "\\right)" else ""}"
}

const val logSimply = false
private var indentX = 0

private fun simplyLog(node: Node, indent: Int): Node {
    println("|       ".repeat(indent) + "Simplify: " + node)
    val s = node.simply()
    println("|       ".repeat(indent) + "|--> " + s)
    --indentX
    return s
}