import groovy.util.Node
import groovy.util.XmlNodePrinter
import java.io.PrintWriter
import java.io.StringWriter

val eachFile: File.((File) -> Unit) -> Unit by extra
val parseXml: (String) -> Node by extra
val getListNode: Node.(String) -> List<Node> by extra
val forEachNode: Node.(String, (Node) -> Unit) -> Unit by extra

fun getXmlsTestResultDirs(files: FileCollection): List<File> {
    val result = mutableListOf<File>()
    val endsWithBinary = "/binary"
    files.forEach {
        if(it.absolutePath.endsWith(endsWithBinary)) {
            val absolutePath = it.absolutePath
            val path = absolutePath.substring(
                startIndex = 0,
                endIndex = absolutePath.length - endsWithBinary.length
            )
            File(path).apply {
                if(exists()) eachFile { file ->
                    if(file.name.endsWith(".xml")) result.add(file)
                }
            }
        }
    }
    return result
}

fun getTestingSignature(files: FileCollection): String {
    return getXmlsTestResultDirs(files).fold("") { accumulator, file ->
        val root = parseXml(file.readText())
        root.attributes().apply {
            remove("timestamp")
            remove("hostname")
            remove("time")
        }
        root.forEachNode("testcase") { testcase ->
            testcase.attributes().remove("time")
            testcase.forEachNode("failure") { failure ->
                failure.setValue(emptyMap<Any?, Any?>())
            }
        }
        accumulator + StringWriter().also { writer ->
            XmlNodePrinter(PrintWriter(writer)).apply {
                isPreserveWhitespace = true
            }.print(root)
        }.toString()
    }
}

fun getTestCoverageResult(filePath: String): Double {
    val counters = parseXml(File(filePath).readText()).getListNode("counter")
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
fun getTestCoverageResultBadgeColor(result: Double): String {
    return when {
        result > 1 || result < 0 -> throw IllegalStateException(
            "Test coverage must be in [0..1] but $result!"
        )
        result < 0.50 -> "d50000"
        result < 0.75 -> "00c853"
        else -> "00c853"
    }
}

fun createBadgeUrl(urlTitle: String, urlValue: String, color: String): String {
    return "https://img.shields.io/badge/$urlTitle-$urlValue-$color.svg?style=flat"
}
fun createBadgeUrlTitleOnly(urlTitle: String, color: String): String {
    return "https://img.shields.io/badge/$urlTitle-$color.svg?style=flat"
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
fun getTestingResultBadgeColor(isPassed: Boolean): String {
    return if(isPassed) "00c853" else "d50000"
}

extra.apply {
    set("getTestingSignature", ::getTestingSignature)
    set("getTestCoverageResultBadgeColor", ::getTestCoverageResultBadgeColor)
    set("getTestCoverageResult", ::getTestCoverageResult)
    set("createBadgeUrl", ::createBadgeUrl)
    set("createBadgeUrlTitleOnly", ::createBadgeUrlTitleOnly)
    set("getTestingResult", ::getTestingResult)
    set("getTestingResultBadgeColor", ::getTestingResultBadgeColor)
}