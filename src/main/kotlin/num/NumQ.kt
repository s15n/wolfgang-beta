package num

abstract class NumQ(p: NumZ, q: NumN) : Num {
    private val gcd = gcd(p.value, q.value.toInt())
    val p = p.value / gcd
    val q = q.value / gcd.toUInt()

    override val is0 = p.is0
    override val is1 = this.p == this.q.toInt()
    override val isN1 = -this.p == this.q.toInt()
    override val isPos = p.isPos
    override val isNeg = p.isNeg

   /* override fun plus(b: Num): Num {
        val add = when (b) {
            is NumN -> NumQ(NumZ(b.value.toInt()), number.one)
            is NumZ ->
        }*/
            /*Q add = switch (num) {
                case N n -> new Q(new Z(n.x), N.ONE);
                case Z z -> new Q(z, N.ONE);
                case Q w -> w;
                default -> null;
            };
            if (add == null)
                return num.plus(this);

            int gcd = MathUtil.gcd(q.x, add.q.x);
            return new Q(new Z(p.x * (add.q.x / gcd) + add.p.x * (q.x / gcd)), new N(q.x * add.q.x / gcd));
        }*/

    override fun laTeX(first: Boolean): String {
        if (q == 1u || p == 0) return NumZ(p).laTeX(first)
        return if (p > 0 || !first) "\\frac{" + NumZ(p).laTeX(true) + "}{" + q + "}" else "-\\frac{" + -p + "}{" + q + "}"
    }

    override fun toString(): String {
        return if (q == 1u) p.toString() else "$p/$q"
    }
}

private fun gcd(x: Int, y: Int): Int {
    TODO()
}