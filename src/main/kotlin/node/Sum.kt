package node

import num.Num

data class Sum(val a: Node, val b: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        return a.derivative(vars) + b.derivative(vars)
    }

    override fun simply(): Node {
        val a = a.simplyLog()
        val b = b.simplyLog()

        return simply(a, b) ?: simply(b, a) ?: (a + b)
    }

    override fun laTeX(first: Boolean): String {
        return "${a.laTeX(first)} + ${b.laTeX(false)}"
    }

    override fun toString(): String {
        return "($a + $b)"
    }
}

private fun simply(a: Node, b: Node): Node? {
    if (a is Num && b is Num)
        return a + b

    if (b is Num && b.is0)
        return a

    if (b is Neg)
        return (a - b.x).simplyLog()
    if (b is Num && b.isNeg)
        return (a - (-b)).simplyLog()

    return null
}