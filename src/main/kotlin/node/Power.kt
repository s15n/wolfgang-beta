package node

import num.Num
import num.number

data class Power(val a: Node, val b: Node) : Node {
    override fun derivative(vars: CharArray): Node {
        if (b is Num)
            return b * a.pow(b - number.one) * a.derivative(vars)
        // a^b * (b' log a + b * a'/a)
        return a.pow(b) * (b.derivative(vars) * Log(a) + b * (a.derivative(vars) / a))
    }

    override fun simply(): Node {
        val a = a.simplyLog()
        val b = b.simplyLog()

        if (b is Num && a !is Num) {
            if (b.is0)
                return number.one
            if (b.is1)
                return a
            if (b.isN1)
                return a.inv()
            if (b.isNeg)
                return (a.pow(-b)).inv()
        }

        if (a is Num) {
            if (a.is0) {
                if (b is Num && b.is0)
                    return Power(a, b)
                return number.zero
            }
            if (a.is1)
                return number.one
            if (b is Num)
                return a.pow(b)
        }

        if (b is Neg)
            return a.pow(b.x).inv().simplyLog()

        if (a is Power) {
            return a.a.pow(a.b * b).simplyLog()
        }

        if (a is Inv) {
            return (number.one / (a.x.pow(b))).simplyLog()
        }

        if (b is Num && a is Prod) {
            val aNum = a.getNum()
            if (aNum != null) {
                val aOther = if (aNum == a.a) a.b else a.a
                return (aNum.pow(b) * aOther.pow(b)).simplyLog()
            }
        }
        if (b is Num && a is Div) {
            if (a.a is Num || a.b is Num)
                return (a.a.pow(b) / a.b.pow(b)).simplyLog()
        }

        return a.pow(b)
    }

    override fun laTeX(first: Boolean): String {
        var aLI = a.isLaTeXInline();

        if (aLI && (a is Div || a is Inv))
            aLI = false

        return "${if (aLI) a.laTeX(first) else "\\left(${a.laTeX(true)}\\right)"}^{${b.laTeX(true)}}"
    }

    override fun isLaTeXInline(): Boolean {
        return true
    }

    override fun toString(): String {
        return "($a ^ $b)"
    }
}