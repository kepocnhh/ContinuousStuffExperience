package testing

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import util.filterByName
import util.tasksWithType

fun Project.createRunTestsTask(
    name: String = "runTests"
) {
    tasks.create(name) {
        val tasks = subprojects.tasksWithType<Test>().filterByName("test")
        val size = tasks.size
        if(size == 0) println("\tno test tasks")
        else {
            for(i in 0 until size-1) {
                tasks[i].finalizedBy(tasks[i+1])
            }
            dependsOn(tasks.first())
        }
    }
}