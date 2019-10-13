package util

import org.gradle.api.Project
import org.gradle.api.Task

inline fun <reified T: Task> Iterable<Project>.tasksWithType() = flatMap {
    it.tasks.withType(T::class.java)
}
fun Iterable<Project>.tasksBy(predicate: (Task) -> Boolean) = flatMap {
    it.tasks.filter(predicate)
}

fun <T: Task> Iterable<T>.filterByName(name: String) = filter { it.name == name }
fun <T: Task> Iterable<T>.containsByName(name: String) = firstOrNull { it.name == name } != null