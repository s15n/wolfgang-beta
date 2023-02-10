package node

import num.number

data class Var(val name: Char) : Node {
    override fun derivative(vars: CharArray): Node {
        return if (name in vars) number.one else number.zero
    }

    override fun simplyLog() = simply()

    override fun simply() = this

    override fun laTeX(first: Boolean): String {
        return "$name"
    }

    override fun isLaTeXInline(): Boolean {
        return true
    }

    override fun toString(): String {
        return "$name"
    }
}