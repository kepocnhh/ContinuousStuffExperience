package util

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

fun Iterable<Project>.withPropertiesNotEmpty(first: String, vararg other: String) = filter { project ->
    project.propertyNotEmptyOrNull(first) != null &&
            other.firstOrNull { key -> project.propertyNotEmptyOrNull(key) == null } == null
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

fun Project.requireProperty(key: String): String {
    return property(key) as? String ?: throw IllegalStateException("Project \"$name\" must have property \"$key\"")
}

fun Project.requirePropertyNotEmpty(key: String): String {
    val result = requireProperty(key)
    check(result.isNotEmpty()) { "Project \"$name\" must have not empty property \"$key\"" }
    return result
}

fun Project.propertyOrNull(key: String): String? {
    return if (!hasProperty(key)) null
    else property(key) as? String
}

fun Project.propertyNotEmptyOrNull(key: String): String? {
    val result = propertyOrNull(key)
    return if (result.isNullOrEmpty()) null else result
}
