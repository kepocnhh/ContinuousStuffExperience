package testing

import groovy.util.XmlNodePrinter
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import util.forEachFile
import util.forEachNode
import util.parseXml

private fun getXmlsTestResultDirs(files: Iterable<File>): List<File> {
    val result = mutableListOf<File>()
    val endsWithBinary = "/binary"
    files.forEach {
        if (it.absolutePath.endsWith(endsWithBinary)) {
            val absolutePath = it.absolutePath
            val path = absolutePath.substring(
                startIndex = 0,
                endIndex = absolutePath.length - endsWithBinary.length
            )
            File(path).apply {
                if (exists()) forEachFile { file ->
                    if (file.name.endsWith(".xml")) result.add(file)
                }
            }
        }
    }
    return result
}

fun getTestingSignature(files: Iterable<File>): String {
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
