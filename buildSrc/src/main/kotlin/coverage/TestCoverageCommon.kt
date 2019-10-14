package coverage

import badge.COLOR_GREEN
import badge.COLOR_RED
import badge.COLOR_YELLOW

fun getTestCoverageResultBadgeColor(result: Double): String {
    check(result in 0.0..1.0) { "Test coverage result must be in [0..1] but $result!" }
    return when {
        result < 0.50 -> COLOR_RED
        result < 0.75 -> COLOR_YELLOW
        else -> COLOR_GREEN
    }
}
