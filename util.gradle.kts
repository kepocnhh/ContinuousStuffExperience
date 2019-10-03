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
//            val path = it.absolutePath[0..-endsWithBinary.length]
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

extra.apply {
    set("getTestingSignature", ::getTestingSignature)
}