package testing

import org.gradle.api.tasks.testing.Test
import java.io.File

fun Iterable<Test>.binResultsDirList() = map {
    it.binResultsDir
}

fun getTestingResult(filePath: String): Boolean {
    val data = File(filePath).readText()
    var index = data.indexOf("\"successRate\"")
    check(index >= 0) { "Tag \"successRate\" must exist!" }
    index = data.indexOf("\"percent\"", index)
    check(index >= 0) { "Tag \"percent\" must exist!" }
    val indexLeft = data.indexOf(">", index)
    check(indexLeft >= 0) { "\">\" after \"percent\" must exist!" }
    val indexRight = data.indexOf("%<", indexLeft)
    check(indexRight >= 0) { "\"%<\" after \">\" must exist!" }
    val result = data.substring(indexLeft + 1, indexRight)
    return result == "100"
}