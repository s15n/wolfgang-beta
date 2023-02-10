package node

import num.number

data class Inv(val x: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        return x.pow(number.nOne).derivative(vars)
    }

    override fun simply(): Node {
        val x = x.simplyLog()

        return x.inv()
    }

    override fun laTeX(first: Boolean): String {
        return "\\frac{1}{${x.laTeX(true)}}"
    }

    override fun isLaTeXInline(): Boolean {
        return true
    }

    override fun toString(): String {
        return "$x^-1"
    }
}