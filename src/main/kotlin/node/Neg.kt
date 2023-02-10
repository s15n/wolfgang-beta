package node

import num.Num

data class Neg(val x: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        return -x.derivative(vars)
    }

    override fun simply(): Node {
        val x = x.simplyLog()

        if (x is Num)
            return -x

        if (x is Prod) {
            val xNum = x.getNum()
            if (xNum != null)
                return (-xNum * (if (xNum == x.a) x.b else x.a)).simplyLog()
        }

        return -x
    }

    override fun laTeX(first: Boolean): String {
        if (x is Prod)
            return (-x.a * x.b).laTeX(first)

        return "${if (first) "" else "\\left("}${laTeXFunction("-", x)}${if (first) "" else "\\right)"}"
    }

    override fun toString(): String {
        return "(-$x)"
    }
}