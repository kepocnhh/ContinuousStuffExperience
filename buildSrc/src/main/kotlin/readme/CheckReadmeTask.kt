package readme

import badge.COLOR_BLUE
import badge.COLOR_GREEN
import badge.COLOR_RED
import badge.createBadgeUrl
import coverage.getTestCoverageResult
import coverage.getTestCoverageResultBadgeColor
import coverage.testCoverageReportSignaturePath
import coverage.testCoverageReportXmlPath
import documentation.documentationSignaturePath
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
import testing.getTestingResult
import testing.testingReportHtmlIndexPath
import testing.testingReportSignaturePath

private fun getTestCoverageBadge(signaturePath: String, reportXmlPath: String): String {
    val result = getTestCoverageResult(File(reportXmlPath))
    val badgeUrl = createBadgeUrl(
        label = "test%20coverage",
        message = (100 * result).toLong().toString() + "%25",
        colorMessage = getTestCoverageResultBadgeColor(result)
    )
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Test coverage signature must not be empty!" }
    val reportUrl = "${Repository.url}/reports/coverage/$hash"
    return "[![test coverage]($badgeUrl)]($reportUrl)"
}

private fun getTestingBadge(signaturePath: String, reportHtmlPath: String): String {
    val isPassed = getTestingResult(reportHtmlPath)
    val resultText = if (isPassed) "passed" else "failed"
    val badgeUrl = createBadgeUrl(
        label = "testing",
        message = resultText,
        colorMessage = if (isPassed) COLOR_GREEN else COLOR_RED
    )
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Testing signature must not be empty!" }
    val reportUrl = "${Repository.url}/reports/testing/$hash"
    return "[![testing]($badgeUrl)]($reportUrl)"
}

fun getDocumentationBadge(signaturePath: String): String {
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Documentation signature must not be empty!" }
    val url = "${Repository.url}/documentation/$hash"
    val badgeUrl = createBadgeUrl(
        message = "documentation",
        color = COLOR_BLUE
    )
    return "[![documentation]($badgeUrl)]($url)"
}

fun Project.createCheckReadmeTask(
    name: String = "checkReadme",
    readmeFullPath: String = "./README.md"
) {
    val errorMessagePrefix = "File readme by path: \"$readmeFullPath\""
    task<DefaultTask>(name) {
        doLast {
            val text = File(readmeFullPath).readText()
            check(text.isNotEmpty()) { "$errorMessagePrefix must not be empty!" }
            val testCoverageBadge = getTestCoverageBadge(
                testCoverageReportSignaturePath,
                testCoverageReportXmlPath
            )
            check(text.contains(testCoverageBadge)) {
                "$errorMessagePrefix must contains test coverage result badge:\n$testCoverageBadge"
            }
            val testingBadge = getTestingBadge(
                testingReportSignaturePath,
                testingReportHtmlIndexPath
            )
            check(text.contains(testingBadge)) {
                "$errorMessagePrefix must contains testing result badge:\n$testingBadge"
            }
            val documentationBadge = getDocumentationBadge(
                documentationSignaturePath
            )
            check(text.contains(documentationBadge)) {
                "$errorMessagePrefix must contains documentation badge:\n$documentationBadge"
            }
            val codeStyleBadge = "[![code style](" + createBadgeUrl(
                label = "code%20style",
                message = "Kotlin%20Coding%20Conventions",
                colorMessage = COLOR_BLUE
            ) + ")](https://kotlinlang.org/docs/reference/coding-conventions.html)"
            check(text.contains(codeStyleBadge)) {
                "$errorMessagePrefix must contains documentation badge:\n$codeStyleBadge"
            }
        }
    }
}
