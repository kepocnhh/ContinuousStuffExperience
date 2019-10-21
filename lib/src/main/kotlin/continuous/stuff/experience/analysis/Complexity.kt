package continuous.stuff.experience.analysis

private fun verifyComplexCondition(it: String) {
    fun String.hasCorrectEnding() = !endsWith("foo") && !endsWith("bar") && !endsWith("_")
//    if (it.startsWith("foo") && !it.endsWith("foo") && !it.endsWith("bar") && !it.endsWith("_")) { // complexity:ComplexCondition
    if (it.startsWith("foo") && !it.hasCorrectEnding()) {
        it.hashCode()
    }
}

interface Bad {
    companion object {
        val badStaticDeclaration = "" // todo
    }

    fun badFun() {
        badStaticDeclaration.hashCode()
    } // todo

    //    val s1: String; val s2: String; val s3: String; val s4: String // complexity:ComplexInterface
    val b1: Boolean
    val b2: Boolean
    val b3: Boolean
    val b4: Boolean
}

private fun verifyComplexMethod() {
// complexity:ComplexMethod
// Conditional statements - if, else if, when
// Loops - for, while, do-while, forEach
// Operators &&, ||
// Exceptions - catch, use
// Scope Functions - let, run, with, apply, and also -> Reference
    var r = 0
    val s = "blalba"
//    if(s.startsWith("a")) r++ // +1
//    else if (s.endsWith("b")) r-- // +2
    for (c in s) { // +1
        when (c) { // +1
//            'b' -> r++ // +1
//            'a' -> r-- // +1
            else -> r *= 0 // +1
        }
    }
//    while (r > 0) r-- // +1
//    do r++ /* +1 */ while (r < 10)
    arrayOf("foo", "bar").forEach { it.hashCode() } // +1
    val b = s.startsWith("a") || s.startsWith("b") && s.endsWith("b") // +2
    try {
        if (b) throw IllegalStateException() // +1
    } catch (e: Exception) { // +1
        s.hashCode()
    } catch (e: Throwable) {
        s.hashCode()
    }
}

private fun verifyLabeledExpression() {
//    loop@ for (r in arrayOf("foo", "bar")) if (r == "bar") break@loop // complexity:LabeledExpression

    arrayOf("foo", "bar").forEach {
        //        if(it.startsWith("f")) return@forEach // complexity:LabeledExpression
    }

    class Outer {
        inner class Inner {
            //            fun inner() = this@Inner // referencing itself, use `this` instead // complexity:LabeledExpression
            fun Outer.inner() = this@Inner // this would reference Int and not Inner

            fun outer() = this@Outer
        }
    }
}

private fun verifyLargeClass() { // complexity:LargeClass
    class LargeClass { // complexity 14
        val i1 = 1
        val i2 = 2
        val i3 = 3
        val i4 = 4
        val s1 = "1"
        val s2 = "2"
        val s3 = "3"
        val s4 = "4"
        val b1 = i1 % 2 == 0
        val b2 = i2 % 2 == 0
        val b3 = i3 % 2 == 0
        val b4 = i4 % 2 == 0
    }
}

// fun f(i1: Int, i2: Int, i3: Int, i4: Int, s1: String, s2: String) {} // complexity:LongParameterList 6
private fun verifyLongParameterList() {
    fun f(i1: Int, i2: Int, i3: Int, i4: Int, s1: String, s2: String, s3: String, s4: String) {
        "1".hashCode()
    } // todo
}

private fun verifyMethodOverloading() {
    abstract class MethodOverloading {
        protected abstract fun f(it: String)
        protected abstract fun f(it: Int)
        protected abstract fun f(it: Double)
        protected abstract fun f(s: String, i: Int)
        protected abstract fun f(s: String, d: Double)
        protected abstract fun f(i: Int, d: Double) // complexity:MethodOverloading 6
    }
}

private fun verifyNestedBlockDepth() {
    if (true) {
        if (false) {
            if (true) {
//                if(false) {} // complexity:NestedBlockDepth 4
            }
        }
    }
}

private fun verifyStringLiteralDuplication() {
    val array = arrayOf("lorem")
    val list = listOf("lorem")
//    val item = "lorem" // complexity:StringLiteralDuplication 3
}
