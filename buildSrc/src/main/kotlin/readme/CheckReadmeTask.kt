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
import util.log

private const val testCoveragePrefix = "![test coverage]"
private const val testingPrefix = "![testing]"
private const val documentationPrefix = "![documentation]"
private const val codeStylePrefix = "![code style]"

private const val DEFAULT_README_FILE_PATH = "./README.md"

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
    return "[$testCoveragePrefix($badgeUrl)]($reportUrl)"
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
    return "[$testingPrefix($badgeUrl)]($reportUrl)"
}

fun getDocumentationBadge(signaturePath: String): String {
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Documentation signature must not be empty!" }
    val url = "${Repository.url}/documentation/$hash"
    val badgeUrl = createBadgeUrl(
        message = "documentation",
        color = COLOR_BLUE
    )
    return "[$documentationPrefix($badgeUrl)]($url)"
}

fun getCodeStyleBadge(): String {
    val url = "https://kotlinlang.org/docs/reference/coding-conventions.html"
    val badgeUrl = createBadgeUrl(
        label = "code%20style",
        message = "Kotlin%20Coding%20Conventions",
        colorMessage = COLOR_BLUE
    )
    return "[$codeStylePrefix($badgeUrl)]($url)"
}

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

private fun Project.verifyFileText(fileFullPath: String, text: String) {
    val errorMessagePrefix = "File by path: \"$fileFullPath\""
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
    val codeStyleBadge = getCodeStyleBadge()
    check(text.contains(codeStyleBadge)) {
        "$errorMessagePrefix must contains documentation badge:\n$codeStyleBadge"
    }
}

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

private fun MutableList<String>.replaceLine(
    substring: CharSequence,
    newLine: String
): ReplaceType {
    val line = filter { it.contains(substring) }.apply {
        check(size < 2) {
            "File text must contains only one line with \"$substring\""
        }
    }.firstOrNull()
    if (line != null) {
        if (line == newLine) return ReplaceType.NONE
        line.apply {
            check(indexOf(substring) == lastIndexOf(substring)) {
                "Line must contains only one \"$substring\""
            }
        }
        for (i in 0 until size) {
            if (get(i) == line) {
                set(i, newLine)
                return ReplaceType.REPLACED
            }
        }
        throw IllegalStateException("not possible")
    } else {
        set(0, newLine)
        return ReplaceType.ADDED
    }
}

private enum class ReplaceType {
    REPLACED, ADDED, NONE
}
