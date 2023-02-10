package num

import kotlin.math.exp
import kotlin.math.ln

class NumZ(val value: Int) : Num {
    override val is0 = value == 0
    override val is1 = value == 1
    override val isN1 = value == -1
    override val isPos = value > 0
    override val isNeg = value < 0

    override fun plus(b: Num) = if (b is NumN || b is NumZ)
        NumZ(value + (if (b is NumN) b.value.toInt() else (b as NumZ).value))
    else (b + this)

    // TODO
    override fun minus(b: Num) = if (b is NumN || b is NumZ)
        NumZ(value - (if (b is NumN) b.value.toInt() else (b as NumZ).value))
    else (approx() - b.approx())

    override fun times(b: Num) = if (b is NumN || b is NumZ)
        NumZ(value * (if (b is NumN) b.value.toInt() else (b as NumZ).value))
    else (b * this)

    // TODO
    override fun div(b: Num) = if (b.is0)
        throw Exception("Divide by 0")
    else if (b is NumN)
        NumApprox(value.toDouble() / b.value.toDouble())
    else if (b is NumZ)
        NumApprox(value.toDouble() / b.value.toDouble())
    else (approx() / b.approx())

    override fun unaryMinus() = NumZ(-value)

    // TODO
    override fun pow(b: Num): Num = if (b is NumN) {
        if (is0)
            number.zero
        else {
            val sign = if (isPos) 1 else if (b.value.toInt() % 2 == 0) 1 else -1
            NumZ(sign * (NumN((if (isPos) value else -value).toUInt()).pow(b) as NumN).value.toInt())
        }
    } else if (b is NumZ) {
        if (b.isPos)
            pow(NumN(b.value.toUInt()))
        else if (b.is0) {
            if (is0)
                throw Exception("0^0 not defined")
            else number.zero
        } else {
            val p = pow(NumN((-value).toUInt())) as NumZ
            val sign = if (p.isPos) 1 else -1
            NumApprox(sign.toDouble() / (sign * p.value).toDouble())
        }
    } else NumApprox(exp(b.approx().value * ln(value.toDouble())))

    // TODO
    override fun inv() = NumApprox(1.0 / value.toDouble())

    override fun approx() = NumApprox(value.toDouble())


    override fun laTeX(first: Boolean): String {
        return if (first) toString() else if (value >= 0) toString() else "(-" + -value + ")"
    }

    override fun toString(): String {
        return "$value"
    }
}