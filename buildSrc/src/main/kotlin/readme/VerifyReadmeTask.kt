package readme

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task

fun Project.createVerifyReadmeTask(
    name: String = "verifyReadme",
    readmeFullPath: String = DEFAULT_README_FILE_PATH
) {
    task<DefaultTask>(name) {
        doLast {
            verifyFileText(readmeFullPath, File(readmeFullPath).readText())
        }
    }
}
