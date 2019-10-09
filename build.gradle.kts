import badge.createBadgeUrl
import coverage.executionDataList
import coverage.getTestCoverageResult
import coverage.getTestCoverageResultBadgeColor
import testing.binResultsDirList
import testing.getTestingResult
import testing.getTestingResultBadgeColor
import testing.getTestingSignature
import util.*

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath(
            group = "org.jetbrains.kotlin",
            name = "kotlin-gradle-plugin",
            version = Version.kotlin
        )
    }
}

plugins {
    id("jacoco")
    id("io.gitlab.arturbosch.detekt") version Version.detekt
    id("org.jetbrains.dokka") version Version.dokka
}

repositories {
    jcenter()
}

evaluationDependsOnChildren()

tasks.create<org.jetbrains.dokka.gradle.DokkaTask>("collectDocumentation") {
    outputFormat = "html"
    outputDirectory = "$documentationPath/html"
    sourceDirs = subprojects.withPlugin("kotlin").srcDirs("main")
    doLast {
        val files = File(documentationPath).listFilesRecurse {
            !it.isDirectory
                && !it.name.contains("index-outline")
                && it.name.endsWith(".html")
        }
        val result = files.sortedByAbsolutePath().fold("") { accumulator, file ->
            accumulator + file.readText()
        }
        File("$documentationPath/signature").rewrite(result.digestSha512())
    }
}

jacoco {
    toolVersion = Version.jacoco
}

detekt {
    toolVersion = Version.detekt
}

tasks.create<io.gitlab.arturbosch.detekt.Detekt>("runDocumentationVerification") {
    setSource(subprojects.withPlugin("kotlin").srcDirs("main"))
    config = files("project/detekt/config/documentation.yml")
    reports {
        html.enabled = false
        xml {
            enabled = true
            destination = File("$analysisDocumentationPath/xml/report.xml")
        }
        txt.enabled = false
    }
}

tasks.create<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}

tasks.create("compile") {
    dependsOn("clean")
    dependsOn(
        subprojects.tasksBy {
            it.name.startsWith("compile")
        }
    )
}

val testingReportPath = "$reportsPath/testing"

tasks.create<TestReport>("collectTestingReport") {
    destinationDir = File("$testingReportPath/html")
    setTestResultDirs(
        subprojects.tasksWithType<Test>().filterByName("test").binResultsDirList()
    )
    doLast {
        File("$testingReportPath/signature").rewrite(
            getTestingSignature(testResultDirs).digestSha512()
        )
    }
}

tasks.create("runTests") {
    val tasks = subprojects.tasksWithType<Test>().filterByName("test")
    val size = tasks.size
    if(size == 0) {
        println("\tno test tasks")
    } else {
        for(i in 0 until size-1) {
            tasks[i].finalizedBy(tasks[i+1])
        }
        dependsOn(tasks.first())
    }
}

val testCoverageReportXmlFullPath = "$testCoverageReportPath/xml/report.xml"

tasks.create<JacocoReport>("collectTestCoverageReport") {
    val testCoverageReportFile = File(testCoverageReportXmlFullPath)
    val projects = subprojects.filter {
        it.hasPlugin("jacoco") && it.tasks.containsByName("test")
    }
    reports {
        xml.isEnabled = true
        xml.destination = testCoverageReportFile
        html.isEnabled = true
        html.destination = File("$testCoverageReportPath/html")
        csv.isEnabled = false
    }
    // getAdditionalSourceDirs().from(files(projects.sourceSets.main.allSource.srcDirs))
    val sourceSets = projects.sourceSets("main")
    val executionDataList = projects.tasksWithType<JacocoReport>().executionDataList()
    sourceDirectories.from(files(sourceSets.srcDirs()))
    classDirectories.from(files(sourceSets.output()))
    executionData.from(files(executionDataList))
    doLast {
        val files = File(testCoverageReportPath).listFilesRecurse {
            !it.isDirectory
                && !it.name.contains("jacoco-sessions")
                && it.name.endsWith(".html")
        }
        val result = files.sortedByAbsolutePath().fold("") { accumulator, file ->
            accumulator + file.readText()
        }
        File("$testCoverageReportPath/signature").rewrite(result.digestSha512())
        val testCoverageResult = getTestCoverageResult(testCoverageReportFile)
        println("\ttest coverage result: " + (100*testCoverageResult).toLong().toString() + "%")
    }
}

tasks.create("runTestCoverageVerification") {
    dependsOn(
        subprojects.tasksBy {
            it.name.startsWith("jacocoTestCoverageVerification")
        }
    )
}

private val repositoryUrl = "https://kepocnhh.github.io/ContinuousStuffExperience"

fun getTestCoverageBadge(signaturePath: String, reportXmlPath: String): String {
    val result = getTestCoverageResult(File(reportXmlPath))
    val badgeUrl = createBadgeUrl(
        "test_coverage",
        (100*result).toLong().toString() + "%25",
        getTestCoverageResultBadgeColor(result)
    )
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Test coverage signature must not be empty!" }
    val reportUrl = "$repositoryUrl/reports/coverage/$hash"
    return "[![test coverage]($badgeUrl)]($reportUrl)"
}

fun getTestingBadge(signaturePath: String, reportHtmlPath: String): String {
    val isPassed = getTestingResult(reportHtmlPath)
    val resultText = if(isPassed) "passed" else "failed"
    val badgeUrl = createBadgeUrl(
        "testing",
        resultText,
        getTestingResultBadgeColor(isPassed)
    )
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Testing signature must not be empty!" }
    val reportUrl = "$repositoryUrl/reports/testing/$hash"
    return "[![testing]($badgeUrl)]($reportUrl)"
}

fun getDocumentationBadge(signaturePath: String): String {
    val hash = File(signaturePath).readText()
    check(hash.isNotEmpty()) { "Documentation signature must not be empty!" }
    val url = "$repositoryUrl/documentation/$hash"
    val badgeUrl = createBadgeUrl(
        "documentation",
        "212121"
    )
    return "[![documentation]($badgeUrl)]($url)"
}

tasks.create("checkReadme") {
    doLast {
        val fileName = "README.md"
        val readmeText = File(fileName).readText()
        check(readmeText.isNotEmpty()) { "File $fileName must not be empty!" }
        val testCoverageBadge = getTestCoverageBadge(
            "$testCoverageReportPath/signature",
            testCoverageReportXmlFullPath
        )
        check(readmeText.contains(testCoverageBadge)) {
            "File $fileName must contains test coverage result badge:\n$testCoverageBadge"
        }
        val testingBadge = getTestingBadge(
            "$testingReportPath/signature",
            "$testingReportPath/html/index.html"
        )
        check(readmeText.contains(testingBadge)) {
            "File $fileName must contains testing result badge:\n$testingBadge"
        }
        val documentationBadge = getDocumentationBadge(
            "$documentationPath/signature"
        )
        check(readmeText.contains(documentationBadge)) {
            "File $fileName must contains documentation badge:\n$documentationBadge"
        }
    }
}