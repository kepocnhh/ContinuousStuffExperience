package coverage

import java.io.File
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
import org.gradle.testing.jacoco.tasks.JacocoReport
import output
import srcDirs
import testCoverageReportPath
import util.containsByName
import util.digestSha512
import util.hasPlugin
import util.listFilesRecurse
import util.rewrite
import util.sortedByAbsolutePath
import util.sourceSets
import util.tasksWithType

val Project.testCoverageReportSignaturePath get() = "$testCoverageReportPath/signature"
val Project.testCoverageReportHtmlPath get() = "$testCoverageReportPath/html"
val Project.testCoverageReportXmlPath get() = "$testCoverageReportPath/xml/report.xml"

fun Project.rewriteTestCoverageReportSignature() {
    val files = File(testCoverageReportPath).listFilesRecurse {
        !it.isDirectory && !it.name.contains("jacoco-sessions") && it.name.endsWith(".html")
    }
    val result = files.sortedByAbsolutePath().fold("") { accumulator, file ->
        accumulator + file.readText()
    }
    File(testCoverageReportSignaturePath).rewrite(result.digestSha512())
}

fun Project.createCollectTestCoverageReportTask(
    name: String = "collectTestCoverageReport"
) {
    task<JacocoReport>(name) {
        val testCoverageReportFile = File(testCoverageReportXmlPath)
        val projects = subprojects.filter {
            it.hasPlugin("jacoco") && it.tasks.containsByName("test")
        }
        reports {
            xml.isEnabled = true
            xml.destination = testCoverageReportFile
            html.isEnabled = true
            html.destination = File(testCoverageReportHtmlPath)
            csv.isEnabled = false
        }
        val sourceSets = projects.sourceSets("main")
        val executionDataList = projects.tasksWithType<JacocoReport>().executionDataList()
        sourceDirectories.from(files(sourceSets.srcDirs()))
        classDirectories.from(files(sourceSets.output()))
        executionData.from(files(executionDataList))
        doLast {
            rewriteTestCoverageReportSignature()
            val testCoverageResult = getTestCoverageResult(testCoverageReportFile)
            println("\ttest coverage result: " + (100 * testCoverageResult).toLong().toString() + "%")
        }
    }
}
