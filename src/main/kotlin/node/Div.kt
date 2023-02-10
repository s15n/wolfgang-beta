package node

import num.Num
import num.number

data class Div(val a: Node, val b: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        return (a.derivative(vars) * b - a * b.derivative(vars)) / b.pow(number[2u])
    }

    override fun simply(): Node {
        val a = a.simplyLog()
        val b = b.simplyLog()

        if (a is Num && b is Num)
            return a / b

        if (a == b)
            return number.one

        if (a is Num)
            return if (a.is1) b.inv() else if (a.is0) number.zero else a * b.inv()

        return (a * b.inv()).simplyLog()
    }

    override fun laTeX(first: Boolean): String {
        return "\\frac{${a.laTeX(true)}}{${b.laTeX(true)}}"
    }

    override fun isLaTeXInline(): Boolean {
        return true
    }

    override fun toString(): String {
        return "($a / $b)"
    }
}