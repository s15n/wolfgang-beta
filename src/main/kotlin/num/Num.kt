package num

import node.Node

interface Num : Node {
    val is0: Boolean
    val is1: Boolean
    val isN1: Boolean
    val isPos: Boolean
    val isNeg: Boolean

    override fun derivative(vars: CharArray): Node {
        return number.zero
    }

    override fun simplyLog() = simply()

    override fun simply() = this

    override fun isLaTeXInline(): Boolean {
        return true
    }

    operator fun plus(b: Num): Num
    operator fun minus(b: Num): Num
    operator fun times(b: Num): Num
    operator fun div(b: Num): Num
    override operator fun unaryMinus(): Num
    fun pow(b: Num): Num
    override fun inv(): Num

    fun approx(): NumApprox

    operator fun compareTo(i: Int): Int {
        if (i == 0) return if (is0) 0 else if (isPos) 1 else -1
        return approx().value.compareTo(i)
    }
}

object number {
    operator fun get(uInt: UInt) = NumN(uInt)
    operator fun get(int: Int) = NumZ(int)

    val one = NumN(1u)
    val zero = NumZ(0)
    val nOne = NumZ(-1)
}

fun parseNum(text: String): Num {
    if (text.toInt() == 0)
        return number.zero
    return NumN(text.toUInt())
}