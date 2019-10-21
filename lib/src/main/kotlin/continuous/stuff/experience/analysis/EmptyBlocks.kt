package continuous.stuff.experience.analysis

private fun verifyEmptyCatchBlock() {
    try {
        "1".toInt()
//    } catch (e: Throwable) {
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    } catch (ignored: Throwable) {
    }
    // empty-blocks:EmptyCatchBlock
    // todo EmptyCatchBlock with comment
}

// class EmptyClassBlock {} // empty-blocks:EmptyClassBlock
// todo EmptyClassBlock with comment
private fun verifyEmptyClassBlock() {
//    class EmptyClassBlock {} // todo
}

private fun verifyEmptyDefaultConstructor() {
//    class EmptyDefaultConstructor() // empty-blocks:EmptyDefaultConstructor
}

private fun verifyEmptyDoWhileBlock() {
//    do {} while (false) // empty-blocks:EmptyDoWhileBlock
// todo EmptyDoWhileBlock with comment
}

private fun verifyEmptyElseBlock() {
    val b = false
    if (b) b.toString()
//    else {} // empty-blocks:EmptyElseBlock
// todo EmptyElseBlock with comment
}

private fun verifyEmptyFinallyBlock() {
//    try { "1".toInt() } finally {} // empty-blocks:EmptyElseBlock
    // todo EmptyFinallyBlock with comment
}

private fun verifyEmptyForBlock() {
    val last = 10
//    for (i in 0..last) {} // empty-blocks:EmptyForBlock
    // todo EmptyForBlock with comment
}

private fun verifyEmptyFunctionBlock() {
    "1".hashCode()
//    fun f() {} // empty-blocks:EmptyFunctionBlock
    // todo EmptyFunctionBlock with comment
}

private fun verifyEmptyIfBlock() {
    val last = 10
//    if (last > 5) {} // empty-blocks:EmptyIfBlock
    // todo EmptyIfBlock with comment
}

private fun verifyEmptyInitBlock() {
    class EmptyInitBlock {
//        init {} // empty-blocks:EmptyInitBlock
    }
    // todo EmptyInitBlock with comment
}

private fun verifyEmptySecondaryConstructor() {
    class EmptySecondaryConstructor {
//        constructor() {} // empty-blocks:EmptySecondaryConstructor
    }
    // todo EmptySecondaryConstructor with comment
}

private fun verifyEmptyWhenBlock() {
    val last = 10
//    when (last > 5) {} // empty-blocks:EmptyWhenBlock
}

private fun verifyEmptyWhileBlock() {
    val last = 10
//    while (last < 5) {} // empty-blocks:EmptyWhileBlock
    // todo EmptyWhileBlock with comment
}
