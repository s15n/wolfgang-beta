package node

import num.Num
import num.number

data class Prod(val a: Node, val b: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        return a.derivative(vars) * b + a * b.derivative(vars)
    }

    override fun simply(): Node {
        val a = a.simplyLog()
        val b = b.simplyLog()

        val s = simply(a, b) ?: simply(b, a) ?: (a * b)

        if (s is Prod) {
            if (s.b is Num)
                return s.b * s.a
            else if (s.a !is Num && s.b is Var)
                return s.b * s.a
        }

        return s
    }

    fun getNum() = if (a is Num) a else if (b is Num) b else null

    override fun laTeX(first: Boolean): String {
        if (b is Inv) {
            if (a is Neg)
                return (-(a.x / b.x)).laTeX(first)
            if (a is Num && a.isNeg)
                return (-(-a / b.x)).laTeX(first)
            return (a / b.x).laTeX(first)
        }

        val op = if (a is Num && b !is Num && !a.is0 && b !is Div) "" else " \\cdot "
        return bracket(a, first) + op + bracket(b, false)
    }

    private fun bracket(n: Node, first: Boolean): String {
        return if (n is Sum || n is Diff)
            "\\left(${n.laTeX(true)}\\right)"
        else
            n.laTeX(first)
    }

    override fun toString(): String {
        return "($a * $b)"
    }
}

private fun simply(a: Node, b: Node): Node? {
    if (a is Num && b is Num)
        return a * b

    if (a is Num) {
        if (a.is0)
            return number.zero
        if (a.is1)
            return b
        if (a.isN1)
            return -b
    }

    if (b is Prod) {
        val bNum = b.getNum()
        if (bNum != null) {
            val bOther = if (bNum == b.a) b.b else b.a

            if (a is Num)
                return ((a * bNum) * bOther).simplyLog()

            return (bNum * (a * bOther)).simplyLog()
        }
    }

    val aa = if (a is Inv) (if (a.x is Power) a.x.a else a.x) else if (a is Power) a.a else a
    val ba = if (b is Inv) (if (b.x is Power) b.x.a else b.x) else if (b is Power) b.a else b

    if (aa == ba) {
        val ab = if (a is Inv) (if (a.x is Power) -a.x.b else number.nOne) else if (a is Power) a.b else number.one
        val bb = if (b is Inv) (if (b.x is Power) -b.x.b else number.nOne) else if (b is Power) b.b else number.one

        return aa.pow(ab + bb).simplyLog()
    }

    if (a is Neg) {
        return if (b is Neg)
            (a.x * b.x).simplyLog()
        else
            (-(a.x * b)).simplyLog()
    }

    return null
}