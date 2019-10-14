package util

import groovy.util.Node
import groovy.util.XmlParser
import groovy.xml.QName

fun parseXml(data: String): Node {
    val xmlParser = XmlParser()
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    return xmlParser.parseText(data)
}

fun Node.getListNode(name: String): List<Node> {
    return getAt(QName(name)).map {
        it as? Node ?: throw IllegalStateException()
    }
}

fun Node.forEachNode(name: String, action: (Node) -> Unit) {
    getAt(QName(name)).forEach {
        it as? Node ?: throw IllegalStateException()
        action(it)
    }
}
