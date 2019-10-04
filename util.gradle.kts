import groovy.util.Node
import groovy.util.XmlNodePrinter
import groovy.util.XmlParser
import groovy.xml.QName
import java.io.PrintWriter
import java.io.StringWriter

fun File.eachFile(action: (File) -> Unit) {
    if(isDirectory) {
        listFiles()?.forEach(action)
    }
}
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
            val dir = File(path)
            if(dir.exists()) {
                dir.eachFile { f ->
                    if(f.name.endsWith(".xml")) {
                        result.add(f)
                    }
                }
            }
        }
    }
    return result
}
fun parseXml(data: String): Node {
    val xmlParser = XmlParser()
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    return xmlParser.parseText(data)
}
fun getTestingSignature(files: FileCollection): String {
    return getXmlsTestResultDirs(files).fold("") { accumulator, file ->
        val root = parseXml(file.readText())
        root.attributes().remove("timestamp")
        root.attributes().remove("hostname")
        root.attributes().remove("time")
        root.getAt(QName("testcase")).forEach { testcase ->
            testcase as? Node ?: throw IllegalStateException()
            testcase.attributes().remove("time")
            testcase.getAt(QName("failure")).forEach { failure ->
                failure as? Node ?: throw IllegalStateException()
                failure.setValue(emptyMap<Any?, Any?>())
            }
        }
        val stringWriter = StringWriter()
        val nodePrinter = XmlNodePrinter(PrintWriter(stringWriter))
        nodePrinter.isPreserveWhitespace = true
        nodePrinter.print(root)
        accumulator + stringWriter.toString()
    }
}

fun getTestCoverageResult(filePath: String): Double {
    val root = parseXml(File(filePath).readText())
    val counters = root.getAt(QName("counter"))
    return counters.fold(1.0) { accumulator, item ->
        item as? Node ?: throw IllegalStateException()
        val coveredString = item.attributes()["covered"] as? String ?: throw IllegalStateException()
        val covered = coveredString.toInt()
        val missed = item.attributes()["missed"] as? String ?: throw IllegalStateException()
        val sum = missed.toInt() + covered
        if(sum > 0) {
            (accumulator + covered / sum) / 2
        } else accumulator
    }
}
fun getTestCoverageResultBadgeColor(result: Double): String {
    return when {
        result > 1 || result < 0 -> throw IllegalStateException("Test coverage must be in [0..1] but $result!")
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
    if(index < 0) throw IllegalStateException("Tag \"successRate\" must exist!")
    index = data.indexOf("\"percent\"", index)
    if(index < 0) throw IllegalStateException("Tag \"percent\" must exist!")
    val indexLeft = data.indexOf(">", index)
    if(indexLeft < 0) throw IllegalStateException("\">\" after \"percent\" must exist!")
    val indexRight = data.indexOf("%<", indexLeft)
    if(indexRight < 0) throw IllegalStateException("\"%<\" after \">\" must exist!")
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