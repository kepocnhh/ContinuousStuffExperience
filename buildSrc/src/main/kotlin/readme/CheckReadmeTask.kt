package readme

import badge.createBadgeUrl
import coverage.*
import documentation.documentationSignaturePath
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.task
import testing.*
import java.io.File

private fun getTestCoverageBadge(signaturePath: String, reportXmlPath: String): String {
    val result = getTestCoverageResult(File(reportXmlPath))
    val badgeUrl = createBadgeUrl(
        "test_coverage",
        (100*result).toLong().toString() + "%25",
        getTestCoverageResultBadgeColor(result)
    )
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Test coverage signature must not be empty!" }
    val reportUrl = "${Repository.url}/reports/coverage/$hash"
    return "[![test coverage]($badgeUrl)]($reportUrl)"
}

private fun getTestingBadge(signaturePath: String, reportHtmlPath: String): String {
    val isPassed = getTestingResult(reportHtmlPath)
    val resultText = if(isPassed) "passed" else "failed"
    val badgeUrl = createBadgeUrl(
        "testing",
        resultText,
        getTestingResultBadgeColor(isPassed)
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
        "documentation",
        "212121"
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
        }
    }
}