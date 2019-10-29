package version

import VERSION_MINOR_KEY
import VERSION_PATCH_KEY
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
import util.PROPERTIES_JSON_FILE_NAME
import versionMinor
import versionPatch

fun Project.createIncrementVersionPatchTask(
    name: String = "incrementVersionPatch",
    propertiesFullPath: String = projectDir.absolutePath + "/" + PROPERTIES_JSON_FILE_NAME
) {
    task<DefaultTask>(name) {
        doLast {
            val versionPatch = versionPatch()
            val file = File(propertiesFullPath)
            check(file.exists()) { "File by path \"${file.absolutePath}\" must exists!" }
            val jsonNode = JsonSlurper().parseText(file.readText())
            check(jsonNode is Map<*, *>)
            val jsonMutableMap = jsonNode.toMutableMap()
            jsonMutableMap[VERSION_PATCH_KEY] = (versionPatch + 1).toString()
            val text = JsonOutput.prettyPrint(JsonOutput.toJson(jsonMutableMap))
            file.delete()
            file.writeText(text)
            println("${project.name}\n\tversion patch successfully increment to ${versionPatch()}")
        }
    }
}

fun Project.createIncrementVersionMinorTask(
    name: String = "incrementVersionMinor",
    propertiesFullPath: String = projectDir.absolutePath + "/" + PROPERTIES_JSON_FILE_NAME
) {
    task<DefaultTask>(name) {
        doLast {
            val versionMinor = versionMinor()
            val file = File(propertiesFullPath)
            check(file.exists()) { "File by path \"${file.absolutePath}\" must exists!" }
            val jsonNode = JsonSlurper().parseText(file.readText())
            check(jsonNode is Map<*, *>)
            val jsonMutableMap = jsonNode.toMutableMap()
            jsonMutableMap[VERSION_PATCH_KEY] = "0"
            jsonMutableMap[VERSION_MINOR_KEY] = (versionMinor + 1).toString()
            val text = JsonOutput.prettyPrint(JsonOutput.toJson(jsonMutableMap))
            file.delete()
            file.writeText(text)
            println("${project.name}\n\tversion minor successfully increment to ${versionMinor()}")
        }
    }
}
