package readme

import coverage.testCoverageReportSignaturePath
import coverage.testCoverageReportXmlPath
import documentation.documentationSignaturePath
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
import testing.testingReportHtmlIndexPath
import testing.testingReportSignaturePath
import util.log
import versionName

fun Project.createFixReadmeTask(
    name: String = "fixReadme",
    readmeFullPath: String = DEFAULT_README_FILE_PATH
) {
    val messagePrefix = "File readme by path: \"$readmeFullPath\""
    task<DefaultTask>(name) {
        doLast {
            val file = File(readmeFullPath)
            val lines = file.readLines().toMutableList()
            check(lines.isNotEmpty() && !lines.all { it.isEmpty() }) { "$messagePrefix must not be empty!" }
            val results = mutableListOf<ReplaceType>()
            lines.replaceLine(
                substring = testCoveragePrefix,
                newLine = getTestCoverageBadge(
                    testCoverageReportSignaturePath,
                    testCoverageReportXmlPath
                )
            ).also {
                results.add(it)
            }
            lines.replaceLine(
                substring = testingPrefix,
                newLine = getTestingBadge(
                    testingReportSignaturePath,
                    testingReportHtmlIndexPath
                )
            ).also {
                results.add(it)
            }
            lines.replaceLine(
                substring = documentationPrefix,
                newLine = getDocumentationBadge(
                    documentationSignaturePath
                )
            ).also {
                results.add(it)
            }
            lines.replaceLine(
                substring = codeStylePrefix,
                newLine = getCodeStyleBadge()
            ).also {
                results.add(it)
            }

            lines.replaceLine(
                substring = currentVersionPrefix,
                newLine = getCurrentVersionBadge(versionName())
            ).also {
                results.add(it)
            }

            if (results.firstOrNull { it != ReplaceType.NONE } != null) {
                val text = lines.reduce { accumulator, line -> accumulator + "\n" + line }
                verifyFileText(readmeFullPath, text)
                file.delete()
                file.writeText(text)
            } else {
                log("$messagePrefix already fixed")
            }
        }
    }
}
