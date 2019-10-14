package util

import org.gradle.api.Project
import org.gradle.api.Task

fun Project.allTasks(): List<Task> {
    val result = mutableListOf<Task>()
    result.addAll(tasks)
    result.addAll(subprojects.tasks())
    return result
}

inline fun <reified T : Task> Project.allTasksWithType(): List<T> {
    val result = mutableListOf<T>()
    result.addAll(tasks.withType(T::class.java))
    result.addAll(subprojects.tasksWithType())
    return result
}

inline fun <reified T : Task> Iterable<Project>.tasksWithType() = flatMap {
    it.tasks.withType(T::class.java)
}

fun Iterable<Project>.tasksBy(predicate: (Task) -> Boolean) = flatMap {
    it.tasks.filter(predicate)
}

fun <T : Task> Iterable<T>.filterByName(name: String) = filter { it.name == name }
fun <T : Task> Iterable<T>.containsByName(name: String) = firstOrNull { it.name == name } != null
