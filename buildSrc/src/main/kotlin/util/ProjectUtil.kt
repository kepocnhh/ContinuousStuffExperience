package util

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the

fun Project.hasPlugin(id: String) = pluginManager.hasPlugin(id)

fun Project.sourceSet(name: String) = the<SourceSetContainer>()[name]!!

fun Iterable<Project>.withPlugin(id: String) = filter {
    it.pluginManager.hasPlugin(id)
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