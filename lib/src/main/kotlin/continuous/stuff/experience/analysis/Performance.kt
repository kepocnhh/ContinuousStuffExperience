package continuous.stuff.experience.analysis

// private fun verifyArrayPrimitive(array: Array<Int>): Array<Double> { // performance:ArrayPrimitive
private fun verifyArrayPrimitive(array: IntArray): DoubleArray {
    return DoubleArray(0)
}

private fun verifyForEachOnRange() {
//    (1..10).forEach { it.toString() } // performance:ForEachOnRange
//    (1 until 10).forEach { it.toString() } // performance:ForEachOnRange
//    (10 downTo 1).forEach { it.toString() } // performance:ForEachOnRange
    for (it in 1..10) {
        it.toString()
    }
}

private fun verifySpreadOperator() {
    fun bar(vararg args: String) = args.forEach { it.hashCode() }
//    val args = arrayOf("value one", "value two"); bar(*args) // performance:SpreadOperator
    bar(*arrayOf("value one", "value two")) // performance:SpreadOperator todo
    bar("value one", "value two")
}

private fun verifyUnnecessaryTemporaryInstantiation() {
//    Integer(1).toString() // performance:UnnecessaryTemporaryInstantiation
    Integer.toString(1)
    1.toString()
}
