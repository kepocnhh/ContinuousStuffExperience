package version

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
import versionMinor
import versionPatch
import java.io.File

fun Project.createIncrementVersionPatchTask(
    name: String = "incrementVersionPatch",
    propertiesFullPath: String = "$projectDir/gradle.properties"
) {
    task<DefaultTask>(name) {
        doLast {
            val versionPatch = versionPatch()
            val file = File(propertiesFullPath)
            check(file.exists()) { "File by path \"${file.absolutePath}\" must exists!" }
            val lines = file.readLines().toMutableList()
            check(lines.isNotEmpty()) { "File by path \"${file.absolutePath}\" must be not empty!" }
            var isFind = false
            for (i in 0 until lines.size) {
                if (lines[i].startsWith("versionPatch=")) {
                    lines[i] = "versionPatch=${versionPatch + 1}"
                    isFind = true
                    break
                }
            }
            check(isFind) { "File by path \"${file.absolutePath}\" must include line with versionPatch!" }
            val text = lines.reduce { accumulator, line -> accumulator + "\n" + line }
            file.delete()
            file.writeText(text)
            println("${project.name}\n\tversion patch successfully increment to ${versionPatch + 1}")
        }
    }
}

fun Project.createIncrementVersionMinorTask(
    name: String = "incrementVersionMinor",
    propertiesFullPath: String = "$projectDir/gradle.properties"
) {
    task<DefaultTask>(name) {
        doLast {
            val versionMinor = versionMinor()
            val file = File(propertiesFullPath)
            check(file.exists()) { "File by path \"${file.absolutePath}\" must exists!" }
            val lines = file.readLines().toMutableList()
            check(lines.isNotEmpty()) { "File by path \"${file.absolutePath}\" must be not empty!" }
            var isPatchFind = false
            var isMinorFind = false
            for (i in 0 until lines.size) {
                if (lines[i].startsWith("versionPatch=")) {
                    lines[i] = "versionPatch=0"
                    isPatchFind = true
                    if(isMinorFind) break
                } else if (lines[i].startsWith("versionMinor=")) {
                    lines[i] = "versionMinor=${versionMinor + 1}"
                    isMinorFind = true
                    if(isPatchFind) break
                }
            }
            check(isPatchFind && isMinorFind) {
                "File by path \"${file.absolutePath}\" must include line with versionPatch and versionMinor!"
            }
            val text = lines.reduce { accumulator, line -> accumulator + "\n" + line }
            file.delete()
            file.writeText(text)
            println("${project.name}\n\tversion minor successfully increment to ${versionMinor + 1}")
        }
    }
}
