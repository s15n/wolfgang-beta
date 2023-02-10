package node

import num.Num
import num.number

data class Diff(val a: Node, val b: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        return a.derivative(vars) - b.derivative(vars)
    }

    override fun simply(): Node {
        val a = a.simplyLog()
        val b = b.simplyLog()

        if (a is Num && b is Num)
            return a - b
        if (b is Num && b.is0)
            return a
        if (a is Num && a.is0)
            return Neg(b)

        if (a == b)
            return number.zero

        if (b is Neg)
            return (a + b.x).simplyLog()
        if (b is Num && b < 0)
            return (a + -b).simplyLog()

        return a - b
    }

    override fun laTeX(first: Boolean): String {
        return "${a.laTeX(first)} - ${if (b is Sum || b is Diff) "\\left(${b.laTeX(true)}\\right)" else b.laTeX(false)}"
    }

    override fun toString(): String {
        return "($a - $b)"
    }
}