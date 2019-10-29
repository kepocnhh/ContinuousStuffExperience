package util

import groovy.json.JsonSlurper
import java.io.File
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the

fun Project.hasPlugin(id: String) = pluginManager.hasPlugin(id)

fun Project.sourceSet(name: String) = the<SourceSetContainer>()[name]!!

fun Project.log(message: String) {
    println("\t$name: $message")
}

fun Project.log(task: Task, message: String) {
    println("\t$name:${task.name}: $message")
}

fun Iterable<Project>.withPlugin(id: String) = filter {
    it.pluginManager.hasPlugin(id)
}

fun Iterable<Project>.withProperties(first: String, vararg other: String) = filter { project ->
    project.propertyOrNull(first) != null && other.firstOrNull { key -> project.propertyOrNull(key) == null } == null
}

fun Iterable<Project>.withPropertiesNotEmpty(first: String, vararg other: String) = filter {
    it.hasPropertiesNotEmpty(first, *other)
}

fun Project.hasPropertiesNotEmpty(first: String, vararg other: String): Boolean {
    return propertyNotEmptyOrNull(first) != null &&
            other.firstOrNull { key -> propertyNotEmptyOrNull(key) == null } == null
}

fun Project.ifHasPropertiesNotEmpty(first: String, vararg other: String, action: Project.() -> Unit) {
    if (hasPropertiesNotEmpty(first, *other)) action()
}

const val PROTECTED_ROOT_NAME = "@:rootProject"

fun Project.protectedName(): String {
    return parent?.let {
        it.protectedName() + "/$name"
    } ?: PROTECTED_ROOT_NAME
}

fun Iterable<Project>.sourceSets(name: String) = map {
    it.sourceSet(name)
}

fun Iterable<Project>.allSource(name: String) = flatMap {
    it.sourceSet(name).allSource
}

fun Iterable<Project>.srcDirs(name: String) = flatMap {
    it.sourceSet(name).allSource.srcDirs
}

fun Iterable<Project>.tasks() = flatMap {
    it.tasks
}

fun Iterable<Project>.tasksByName(name: String) = tasksBy { it.name == name }

const val PROPERTIES_JSON_FILE_NAME = "properties.json"

fun Project.verifyPropertiesJson() {
    val file = File(projectDir.absolutePath + "/$PROPERTIES_JSON_FILE_NAME")
    if (file.exists()) when (val jsonNode = JsonSlurper().parseText(file.readText())) {
        is Map<*, *> -> {
            jsonNode.forEach { (key, value) ->
                check(key is String) { "key must be string" }
                if (value != null) check(value is String) {
                    "value by \"$key\" must be `null` or `string` but it is `${value::class.java.name}`"
                }
            }
        }
        else -> throw IllegalStateException("file \"$PROPERTIES_JSON_FILE_NAME\" must contains json object")
    }
}

fun Project.propertyOrNull(key: String): String? {
    val file = File(projectDir.absolutePath + "/$PROPERTIES_JSON_FILE_NAME")
    if (!file.exists()) return null
    when (val jsonNode = JsonSlurper().parseText(file.readText())) {
        is Map<*, *> -> {
            return jsonNode[key]?.let { it as String }
        }
        else -> throw IllegalStateException("file \"$PROPERTIES_JSON_FILE_NAME\" must contains json object")
    }
}

fun Project.propertyNotEmptyOrNull(key: String): String? {
    val result = propertyOrNull(key)
    return if (result.isNullOrEmpty()) null else result
}

fun Project.requireProperty(key: String): String {
    return propertyOrNull(key) ?: throw IllegalStateException("Project \"$name\" must have property \"$key\"")
}

fun Project.requirePropertyNotEmpty(key: String): String {
    val result = requireProperty(key)
    check(result.isNotEmpty()) { "Project \"$name\" must have not empty property \"$key\"" }
    return result
}

fun Project.allProjects(): List<Project> {
    val result = mutableListOf<Project>()
    result.add(rootProject)
    result.addAll(rootProject.subprojects)
    return result
}
