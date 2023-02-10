package num

import kotlin.math.exp
import kotlin.math.ln

class NumN(val value: UInt) : Num {
    override val is0 = value == 0u
    override val is1 = value == 1u
    override val isN1 = false
    override val isPos = true
    override val isNeg = false

    init {
        if (is0) throw IllegalArgumentException("Natural Number must be > 0")
    }

    override fun plus(b: Num) = if (b is NumN)
        NumN(value + b.value)
    else (b + this)

    override fun minus(b: Num) = if (b is NumN)
        NumZ(value.toInt() - b.value.toInt())
    else (NumZ(value.toInt()) - b)

    override fun times(b: Num) = if (b is NumN)
        NumN(value * b.value)
    else (b * this)

    // TODO
    override fun div(b: Num) = if (b is NumN)
        NumApprox(value.toDouble() / b.value.toDouble())
    else (NumZ(value.toInt()) / b)

    override fun unaryMinus() = NumZ(-value.toInt())

    override fun pow(b: Num) = if (b is NumN)
        NumN((exp(b.value.toDouble() * ln(value.toDouble())) + 0.5).toUInt())
    else (NumZ(value.toInt()).pow(b))

    // TODO
    override fun inv() = NumApprox(1.0 / value.toDouble())

    override fun approx() = NumApprox(value.toDouble())

    override fun laTeX(first: Boolean): String {
        return "$value"
    }

    override fun toString(): String {
        return "$value"
    }
}