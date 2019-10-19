package testing

import groovy.util.Node
import groovy.util.NodeList
import groovy.util.XmlNodePrinter
import util.digestSha512
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
private val allowedTestCaseNames = setOf("failure")
fun getTestingSignature(files: Iterable<File>): String {
    return getXmlsTestResultDirs(files).sortedBy { it.absolutePath }.fold("") { accumulator, file ->
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
            node.children().also { children ->
                children.toList().forEach { child ->
                    child as? Node ?: throw IllegalStateException()
                    if (child.name() !in allowedTestCaseNames) children.remove(child)
                }
            }
            node.forEachNode("failure") {
                it.setValue(Node(node, ""))
            }
        }
        val signature = root.signatureRecurse()
        val result = accumulator + signature
        println("\n\t###\t###\t###")
        println("result: ${file.name}\n${signature.replace(" ", "@")}")
        println("\n\t###\t###\t###")
        println("hash: ${signature.digestSha512()}")
        println("\n\t###\t###\t###")
        result
    }
}

private fun Node.signatureRecurse(): String {
    var result = name().toString()
    result += attributes().toList().sortedBy { (key, _) -> key.toString() }.fold("") { signature, (key, value) ->
        signature + "_" + key.toString() + value.toString()
    }
    children().map {
        it as? Node ?: throw IllegalStateException("child of $this must be groovy.util.Node")
    }.sortedBy { it.name().toString() }.forEach {
        if (it.attributes().isNotEmpty() || it.children().isNotEmpty()) {
            result += "_" + it.signatureRecurse()
        }
    }
    return result
}
