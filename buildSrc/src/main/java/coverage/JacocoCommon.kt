package coverage

import org.gradle.testing.jacoco.tasks.JacocoReport
import util.getListNode
import util.parseXml
import java.io.File

fun Iterable<JacocoReport>.executionDataList() = map {
    it.executionData
}

fun getTestCoverageResult(file: File): Double {
    val counters = parseXml(file.readText()).getListNode("counter")
    val result = counters.sumByDouble {
        val covered: String by it.attributes()
        val coveredDouble = covered.toDouble()
        val missed: String by it.attributes()
        coveredDouble / (missed.toDouble() + coveredDouble)
    }
    return result / counters.size
}
