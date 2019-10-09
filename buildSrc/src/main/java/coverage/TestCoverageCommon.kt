package coverage

fun getTestCoverageResultBadgeColor(result: Double): String {
    check(result in 0.0..1.0) { "Test coverage result must be in [0..1] but $result!" }
    return when {
        result < 0.50 -> "d50000"
        result < 0.75 -> "00c853"
        else -> "00c853"
    }
}