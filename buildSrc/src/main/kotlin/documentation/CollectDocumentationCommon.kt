package documentation

import documentationPath
import java.io.File
import org.gradle.api.Project
import util.digestSha512
import util.listFilesRecurse
import util.rewrite
import util.sortedByAbsolutePath

val Project.documentationSignaturePath get() = "$documentationPath/signature"
val Project.documentationHtmlPath get() = "$documentationPath/html"

fun Project.rewriteDocumentationSignature() {
    val files = File(documentationPath).listFilesRecurse {
        !it.isDirectory && !it.name.contains("index-outline") && it.name.endsWith(".html")
    }
    val result = files.sortedByAbsolutePath().fold("") { accumulator, file ->
        accumulator + file.readText()
    }
    File(documentationSignaturePath).rewrite(result.digestSha512())
}
