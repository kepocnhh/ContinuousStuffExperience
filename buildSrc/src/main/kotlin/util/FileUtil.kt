package util

import java.io.File
import java.nio.charset.Charset

fun File.listFilesRecurse(predicate: (File) -> Boolean): List<File> {
    val result = mutableListOf<File>()
    forEachFileRecurse {
        if (predicate(it)) result.add(it)
    }
    return result
}

fun File.listFilesRecurse(): List<File> {
    val result = mutableListOf<File>()
    forEachFileRecurse {
        result.add(it)
    }
    return result
}

fun File.forEachFileRecurse(action: (File) -> Unit) {
    if (isDirectory) listFiles()?.forEach {
        it.forEachFileRecurse(action)
    } else action(this)
}

fun File.forEachFile(action: (File) -> Unit) {
    if (isDirectory) listFiles()?.forEach(action)
}

fun List<File>.sortedByAbsolutePath() = sortedBy { it.absolutePath }

fun File.rewrite(text: String, charset: Charset = Charsets.UTF_8) {
    delete()
    writeText(text, charset)
}