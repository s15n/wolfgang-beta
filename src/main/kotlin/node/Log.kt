package node

import num.Num
import num.number

data class Log(val x: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        return x.derivative(vars) / x
    }

    override fun simply(): Node {
        val x = x.simplyLog()

        if (x is Num && x.is1)
            return number.zero

        return Log(x)
    }

    override fun laTeX(first: Boolean): String {
        return laTeXFunction("\\log", x)
    }

    override fun toString(): String {
        return "log($x)"
    }
}