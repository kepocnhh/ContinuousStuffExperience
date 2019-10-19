package testing

import groovy.util.Node
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

private val allowedKeys = setOf("name", "tests", "skipped", "failures", "errors")
private val allowedNames = setOf("testcase")
private val allowedTestCaseKeys = setOf("name", "classname")
fun getTestingSignature(files: Iterable<File>): String {
    return getXmlsTestResultDirs(files).fold("") { accumulator, file ->
        val root = parseXml(file.readText())
        root.attributes().also { attributes ->
            attributes.keys.toList().forEach { key ->
                if (key !in allowedKeys) attributes.remove(key)
            }
        }
        root.children().also { children ->
            children.toList().forEach { child ->
                child as? Node ?: throw IllegalStateException()
                if (child.name() !in allowedNames) children.remove(child)
            }
        }
        root.forEachNode("testcase") { node ->
            node.attributes().also { attributes ->
                attributes.keys.toList().forEach { key ->
                    if (key !in allowedTestCaseKeys) attributes.remove(key)
                }
            }
            node.forEachNode("failure") {
                it.setValue(emptyMap<Any?, Any?>())
            }
        }
        accumulator + StringWriter().also { writer ->
            XmlNodePrinter(PrintWriter(writer)).apply {
                isPreserveWhitespace = true
            }.print(root)
        }.toString()
    }
}
