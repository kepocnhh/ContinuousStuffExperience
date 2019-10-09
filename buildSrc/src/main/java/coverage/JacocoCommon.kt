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
    return counters.fold(1.0) { accumulator, item ->
        val covered: String by item.attributes()
        val coveredInt = covered.toInt()
        val missed: String by item.attributes()
        val sum = missed.toInt() + coveredInt
        if(sum > 0) {
            (accumulator + coveredInt / sum) / 2
        } else accumulator
    }
}
