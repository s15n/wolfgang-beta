package num

import kotlin.math.exp
import kotlin.math.ln

class NumApprox(val value: Double) : Num {
    override val is0 = value == 0.0
    override val is1 = value == 1.0
    override val isN1 = value == -1.0
    override val isPos = value > 0
    override val isNeg = value < 0


    override fun plus(b: Num) = NumApprox(value + b.approx().value)

    override fun minus(b: Num) = NumApprox(value - b.approx().value)

    override fun times(b: Num) = NumApprox(value * b.approx().value)

    override fun div(b: Num) = NumApprox(value / b.approx().value)

    override fun unaryMinus() = NumApprox(-value)

    override fun pow(b: Num) = NumApprox(exp(b.approx().value * ln(value)))

    override fun inv() = NumApprox(1.0 / value)

    override fun approx() = this


    override fun laTeX(first: Boolean): String {
        return "$value"
    }

    override fun toString(): String {
        return "$value"
    }
}