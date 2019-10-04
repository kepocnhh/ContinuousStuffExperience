import groovy.util.Node
import groovy.util.XmlParser
import groovy.xml.QName

fun parseXml(data: String): Node {
    val xmlParser = XmlParser()
    xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    xmlParser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    return xmlParser.parseText(data)
}

fun getListNode(root: Node, name: String): List<Node> {
    return root.getAt(QName(name)).map {
        it as? Node ?: throw IllegalStateException()
    }
}

fun forEachNode(root: Node, name: String, action: (Node) -> Unit) {
    root.getAt(QName(name)).forEach {
        it as? Node ?: throw IllegalStateException()
        action(it)
    }
}

extra.apply {
    set("parseXml", ::parseXml)
    set("getListNode", ::getListNode)
    set("forEachNode", ::forEachNode)
}