package continuous.stuff.experience.analysis

import java.util.Date

/**
 * This function is for testing Deprecation analysis.
 * @see `ClosedRange<Double>.contains(Int)`
 * @see Date
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun deprecationSample() {
    val r = 1 in 0.0..5.0
    Date(2019, 5, 5)
} // potential-bugs:Deprecation
// todo https://github.com/arturbosch/detekt/issues/2038

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
 * This function is for sample [SomeEnum] in when operator.
 * @see SomeEnum
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun whenSample() {
    val someEnum = SomeEnum.values().first()
    when (someEnum) {
        SomeEnum.ITEM_1 -> {
            println("it is item 1")
        }
        SomeEnum.ITEM_2 -> {
            println("it is item 2")
        }
//        WhenEnum.ITEM_2 -> {
//            println("it is item 2")
//        } // potential-bugs:DuplicateCaseInWhenExpression
        SomeEnum.ITEM_3 -> {
            println("it is item 3")
        } // potential-bugs:MissingWhenCase
//        else -> {} // potential-bugs:RedundantElseInWhen
    }
} // todo https://github.com/arturbosch/detekt/issues/2041

/**
 * This class is for testing
 * WrongEqualsTypeParameter, EqualsAlwaysReturnsTrueOrFalse, EqualsWithHashCodeExist
 * analysis.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private class VerifyEqualsHashCode {
//    private fun equals(other: String) = super.equals(other)//potential-bugs:WrongEqualsTypeParameter

    override fun equals(other: Any?): Boolean {
//        return true // potential-bugs:EqualsAlwaysReturnsTrueOrFalse
        return when (other) {
            is VerifyEqualsHashCode -> true
            else -> false
        }
    }

    override fun hashCode() = super.hashCode() // potential-bugs:EqualsWithHashCodeExist
}

/**
 * This function is for testing ExplicitGarbageCollectionCall analysis.
 * @see System.gc
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun explicitGarbageCollectionCall() {
//    System.gc()//potential-bugs:ExplicitGarbageCollectionCall
}

/**
 * This function is for testing HasPlatformType analysis.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun getPlatformType() = System.getProperty("propertyName") // potential-bugs:HasPlatformType
// todo https://github.com/arturbosch/detekt/issues/2042

/**
 * This function is for testing InvalidRange analysis.
 * @see until
 * @see rangeTo
 * @see downTo
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun verifyInvalidRange() {
//    for (i in 2 until 1) {/*nop*/}
//    for (i in 2..1) {/*nop*/}
//    for (i in 1 downTo 2) {/*nop*/}
//    val r1 = 2 until 1
//    val r2 = 2..1
//    val r3 = 1 downTo 2
}
// todo https://github.com/arturbosch/detekt/issues/2044
// potential-bugs:InvalidRange

/**
 * This class is for testing
 * IteratorNotThrowingNoSuchElementException, IteratorHasNextCallsNextMethod
 * analysis.
 * @see Iterator
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private class SomeIterator : Iterator<String> {
    private var isNextCalled = false
    override fun next(): String {
        if (isNextCalled) throw NoSuchElementException() // potential-bugs:IteratorNotThrowingNoSuchElementException
        isNextCalled = true
        return "blabla"
    }

    override fun hasNext(): Boolean {
//        val item = next()//potential-bugs:IteratorHasNextCallsNextMethod
        return !isNextCalled
    }
}

/**
 * This function is for testing UnconditionalJumpStatementInLoop analysis.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun unconditionalJumpStatementInLoop() {
//    for (i in 1..2) break//potential-bugs:UnconditionalJumpStatementInLoop
}

/**
 * This function is for testing UnreachableCode analysis.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun unreachableCode() {
//    for (i in 1..2) {break; println()}
//    if (true) {throw IllegalArgumentException(); println()}
//    if (true) {return; println() }
}
// potential-bugs:UnreachableCode

/**
 * This function is for testing UnsafeCallOnNullableType analysis.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun unsafeCallOnNullableType() {
    val someEnum: SomeEnum? = SomeEnum.values().firstOrNull()
//    val someEnum: SomeEnum? = if(System.currentTimeMillis().toInt() % 2 == 0) null else SomeEnum.ITEM_1
    println(someEnum!!.name)
}
// todo https://github.com/arturbosch/detekt/issues/2043
// potential-bugs:UnsafeCallOnNullableType

/**
 * This function is for testing UnsafeCast analysis.
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun unsafeCast() {
    val someEnum: Any = SomeEnum.values().first().ordinal
    println(someEnum as String)
    println(someEnum as VerifyEqualsHashCode)
}
// todo https://github.com/arturbosch/detekt/issues/2043
// potential-bugs:UnsafeCast

/**
 * This function is for testing UselessPostfixExpression analysis.
 * @see inc
 * @version 0.00.01
 * @since ContinuousStuffExperience:lib 0.00.01
 */
private fun uselessPostfixExpression() {
    var i = 0
//    i = i++//potential-bugs:UselessPostfixExpression
    println(i)
}
