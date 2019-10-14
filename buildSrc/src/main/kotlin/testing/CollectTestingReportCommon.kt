package testing

import java.io.File
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.task
import testingReportPath
import util.digestSha512
import util.filterByName
import util.rewrite
import util.tasksWithType

val Project.testingReportSignaturePath get() = "$testingReportPath/signature"
val Project.testingReportHtmlPath get() = "$testingReportPath/html"
val Project.testingReportHtmlIndexPath get() = "$testingReportHtmlPath/index.html"

fun Project.rewriteTestingReportSignature(testResultDirs: Iterable<File>) {
    File(testingReportSignaturePath).rewrite(
        getTestingSignature(testResultDirs).digestSha512()
    )
}

fun Project.createCollectTestingReportTask(
    name: String = "collectTestingReport"
) {
    task<TestReport>(name) {
        destinationDir = File(testingReportHtmlPath)
        setTestResultDirs(
            subprojects.tasksWithType<Test>().filterByName("test").binResultsDirList()
        )
        doLast {
            rewriteTestingReportSignature(testResultDirs)
        }
    }
}
