package continuous.stuff.experience.analysis

import java.util.Date

/**
 * This method is for testing Deprecation analysis.
 * @see `ClosedRange<Double>.contains(Int)`
 * @see Date
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun deprecationSample() {
    val r = 1 in 0.0..5.0
    Date(2019, 5, 5)
}
// todo https://github.com/arturbosch/detekt/issues/2038
// potential-bugs:Deprecation

private enum class Color {
    RED,
    GREEN,
    BLUE;

    private fun privateTest() = Unit
}

private fun whenExhaustive(c: Color) {
    when (c) {
        Color.BLUE -> {
        }
        Color.GREEN -> {
        }
    }
}
// todo
// potential-bugs:MissingWhenCase

private fun whenElseRedundant(c: Color) {
    when (c) {
        Color.BLUE -> {
        }
        Color.GREEN -> {
        }
        Color.RED -> {
        }
        else -> {
        }
    }
}
// todo
// potential-bugs:RedundantElseInWhen

/**
 * This internal class is for using in when operator.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private enum class SomeEnum {
    ITEM_1,
    ITEM_2,
    ITEM_3,
}

/**
 * This method is for sample [SomeEnum] in when operator.
 * @see SomeEnum
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun whenSample() {
    val whenEnum = SomeEnum.values().first()
    when (whenEnum) {
        SomeEnum.ITEM_1 -> {
            println("it is item 1")
        }
        SomeEnum.ITEM_2 -> {
            println("it is item 2")
        }
//        WhenEnum.ITEM_2 -> {//potential-bugs:DuplicateCaseInWhenExpression
//            println("it is item 2")
//        }
    }
}

private class VerifyEqualsHashCode {
//    private fun equals(other: String) = super.equals(other)//potential-bugs:WrongEqualsTypeParameter

    override fun equals(other: Any?): Boolean {
//        return true//potential-bugs:EqualsAlwaysReturnsTrueOrFalse
        return when (other) {
            is VerifyEqualsHashCode -> true
//            is String -> true//todo
            else -> false
        }
    }

    override fun hashCode() = super.hashCode() // potential-bugs:EqualsWithHashCodeExist
}

private fun explicitGarbageCollectionCall() {
//    System.gc()//potential-bugs:ExplicitGarbageCollectionCall
}

private fun getPlatformType() = System.getProperty("propertyName")
// todo
// potential-bugs:HasPlatformType

private fun verifyInvalidRange() {
//    for (i in 2 until 1) {/*nop*/}
//    for (i in 2..1) {/*nop*/}
//    for (i in 1 downTo 2) {/*nop*/}
//    val r1 = 2 until 1
//    val r2 = 2..1
//    val r3 = 1 downTo 2
}
// todo `2 until 2` should be empty
// potential-bugs:InvalidRange

private class SomeIterator : Iterator<String> {
    private var isNextCalled = false
    override fun next(): String {
        if (!isNextCalled) throw NoSuchElementException() // potential-bugs:IteratorNotThrowingNoSuchElementException
        isNextCalled = true
        return "blabla"
    }

    override fun hasNext(): Boolean {
//        val item = next()//potential-bugs:IteratorHasNextCallsNextMethod
        return !isNextCalled
    }
}

private fun unconditionalJumpStatementInLoop() {
//    for (i in 1..2) break//potential-bugs:UnconditionalJumpStatementInLoop
}

private fun unreachableCode() {
//    for (i in 1..2) {break; println()}
//    if (true) {throw IllegalArgumentException(); println()}
//    if (true) {return; println() }
}
// potential-bugs:UnreachableCode

private fun unsafeCallOnNullableType() {
    val whenEnum: SomeEnum? = SomeEnum.values().firstOrNull()
//    val whenEnum: SomeEnum? = if(System.currentTimeMillis().toInt() % 2 == 0) null else SomeEnum.ITEM_1
    println(whenEnum!!.name)
}
// todo
// potential-bugs:UnsafeCallOnNullableType

private fun unsafeCast() {
    val whenEnum: Any = SomeEnum.values().first().ordinal
    println(whenEnum as String)
    println(whenEnum as VerifyEqualsHashCode)
}
// todo
// potential-bugs:UnsafeCast

private fun uselessPostfixExpression() {
    var i = 0
//    i = i++//potential-bugs:UselessPostfixExpression
    println(i)
}
