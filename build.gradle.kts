import java.security.MessageDigest
import java.util.*

buildscript {
    val kotlinVersion = "1.3.50"
    mapOf(
        "jacocoVersion" to "0.8.4",
        "detektVersion" to "1.0.1",
        "kotlinVersion" to kotlinVersion
    ).forEach { (key, value) ->
        extra.set(key, value)
    }

    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("jacoco")
    id("io.gitlab.arturbosch.detekt") version "1.0.1"
    id("org.jetbrains.dokka") version "0.9.18"
}

repositories {
    jcenter()
}

apply(from = "util.gradle.kts")

evaluationDependsOnChildren()

val documentationPath = "$buildDir/documentation"

fun File.eachFileRecurse(action: (File) -> Unit) {
    if(isDirectory) {
        listFiles()?.forEach {
            it.eachFileRecurse(action)
        }
    } else {
        action(this)
    }
}
enum class DigestType(val messageDigestValue: String) {
    SHA_512("SHA-512")
}
val messageDigestSha512 = MessageDigest.getInstance(DigestType.SHA_512.messageDigestValue)!!
val HEX_CHARS = "0123456789ABCDEF"
fun ByteArray.toHexString(): String {
    val builder = StringBuilder(size * 2)
    forEach {
        val i = it.toInt()
        builder.append(HEX_CHARS[i shr 4 and 0x0f]).append(HEX_CHARS[i and 0x0f])
    }
    return builder.toString().toLowerCase(Locale.US)
}
fun String.digest(type: DigestType): String {
    val bytes = when(type) {
        DigestType.SHA_512 -> messageDigestSha512.digest(toByteArray())
    }
    return bytes.toHexString()
}

tasks.create<org.jetbrains.dokka.gradle.DokkaTask>("collectDocumentation") {
    outputFormat = "html"
    outputDirectory = "$documentationPath/html"
    sourceDirs = files(
        subprojects.filter {
            it.pluginManager.hasPlugin("kotlin")
        }.flatMap {
//            it.the<SourceSetContainer>()["main/kotlin"].allSource.srcDirs//todo
            it.the<SourceSetContainer>()["main"].allSource.srcDirs
        }
    )
    doLast {
        val files = mutableListOf<File>()
        file(documentationPath).eachFileRecurse {
            if(!it.isDirectory
                && !it.name.contains("index-outline")
                && it.name.endsWith(".html")) {
                files.add(it)
            }
        }
        val result = files.sortedBy { it.absolutePath }.fold("") { accumulator, file ->
            accumulator + file.readText()
        }
        val signatureFile = File("$documentationPath/signature")
        signatureFile.delete()
        signatureFile.writeText(result.digest(DigestType.SHA_512))
    }
}

jacoco {
    toolVersion = extra["jacocoVersion"] as String
}

private val reportsPath = "$buildDir/reports"
private val analysisPath = "$reportsPath/analysis"
val analysisDocumentationPath = "$analysisPath/documentation"

detekt {
    toolVersion = extra["detektVersion"] as String
}

tasks.create<io.gitlab.arturbosch.detekt.Detekt>("runDocumentationVerification") {
    source = fileTree(
        subprojects.filter {
            it.pluginManager.hasPlugin("kotlin")
        }.flatMap {
            it.the<SourceSetContainer>()["main"].allSource.srcDirs
        }
    )
    config = files("./detekt-config-documentation.yml")
    reports {
        html.enabled = false
        xml {
            enabled = true
            destination = file("$analysisDocumentationPath/xml/report.xml")
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
        subprojects.flatMap {
            it.tasks
        }.filter {
            it.name.startsWith("compile")
        }
    )
}

val testingReportPath = "$reportsPath/testing"
val getTestingSignature: (FileCollection) -> String by ext

tasks.create<TestReport>("collectTestingReport") {
    destinationDir = file("$testingReportPath/html")
    setTestResultDirs(
        subprojects.flatMap {
            it.tasks
        }.filter {
            it.name == "test"
        }.filterIsInstance<Test>().map {
            it.binResultsDir
        }
    )
    doLast {
        val signatureFile = File("$testingReportPath/signature")
        signatureFile.delete()
        signatureFile.writeText(getTestingSignature(testResultDirs).digest(DigestType.SHA_512))
    }
}

tasks.create("runTests") {
    val tasks = subprojects.flatMap {
        it.tasks
    }.filter {
        it.name == "test"
    }.filterIsInstance<Test>()
    val size = tasks.size
    if(size == 0) {
        println("\tno test tasks")
    } else {
        for(i in 0 until size-1) {
//    for(val i=0; i<size-1; i++) {
            tasks[i].finalizedBy(tasks[i+1])
        }
        dependsOn(tasks.first())
    }
}
//task runTests {
//	def tasks = subprojects*.test
//	def size = tasks.size
//	if(size == 0) {
//    	println "\tno test tasks"
//		return
//	}
//	for(def i=0; i<size-1; i++) {
//		tasks[i].finalizedBy tasks[i+1]
//	}
//    dependsOn tasks.first()
//}

val testCoverageReportPath = "$reportsPath/coverage"
val testCoverageReportXmlFullPath = "$testCoverageReportPath/xml/report.xml"
private val getTestCoverageResult: (String) -> Double by ext

tasks.create<JacocoReport>("collectTestCoverageReport") {
    val projects = subprojects.filter { project ->
        project.pluginManager.hasPlugin("jacoco") && project.tasks.find {
            it.name == "test"
        } != null
    }
    reports {
        xml.isEnabled = true
        xml.destination = file(testCoverageReportXmlFullPath)
        html.isEnabled = true
        html.destination = file("$testCoverageReportPath/html")
        csv.isEnabled = false
    }
    // getAdditionalSourceDirs().from(files(projects.sourceSets.main.allSource.srcDirs))
    val sourceSets = projects.map {
        it.the<SourceSetContainer>()["main"]
    }
    val srcDirs = sourceSets.flatMap {
        it.allSource.srcDirs
    }
    val output = sourceSets.flatMap {
        it.output
    }
    val executionDataList = projects.flatMap {
        it.tasks
    }.filterIsInstance<JacocoReport>().map {
        it.executionData
    }
    sourceDirectories.from(files(srcDirs))
    classDirectories.from(files(output))
    executionData.from(files(executionDataList))
    doLast {
        val files = mutableListOf<File>()
        file(testCoverageReportPath).eachFileRecurse {
            if(!it.isDirectory
                && !it.name.contains("jacoco-sessions")
                && it.name.endsWith(".html")) {
                files.add(it)
            }
        }
        val result = files.sortedBy { it.absolutePath }.fold("") { accumulator, file ->
            accumulator + file.readText()
        }
        val signatureFile = file("$testCoverageReportPath/signature")
        signatureFile.delete()
        signatureFile.writeText(result.digest(DigestType.SHA_512))
        val testCoverageResult = getTestCoverageResult(testCoverageReportXmlFullPath)
        println("\ttest coverage result: " + (100*testCoverageResult).toLong().toString() + "%")
    }
}
//task collectTestCoverageReport(type: JacocoReport) {
//    def projects = subprojects.findAll {
//        it.pluginManager.hasPlugin("jacoco") && it.tasks.find { it.name == "test" } != null
//    }
//    reports {
//        xml.enabled true
//        xml.destination file(testCoverageReportXmlFullPath)
//        html.enabled true
//        html.destination file("$testCoverageReportPath/html")
//        csv.enabled false
//    }
//    // getAdditionalSourceDirs().from(files(projects.sourceSets.main.allSource.srcDirs))
//    getSourceDirectories().from(files(projects.sourceSets.main.allSource.srcDirs))
//    getClassDirectories().from(files(projects.sourceSets.main.output))
//    getExecutionData().from(files(projects.jacocoTestReport.executionData))
//    doLast {
//        def files = []
//        file(testCoverageReportPath).eachFileRecurse {
//            if(!it.isDirectory())
//            if(!it.name.contains("jacoco-sessions"))
//            if(it.name.endsWith(".html")) {
//                files.add(it)
//            }
//        }
//        def result = files.sort { it.absolutePath }.inject("") { accumulator, file ->
//            accumulator + getFileText(file.absolutePath)
//        }
//        def signatureFile = file("$testCoverageReportPath/signature")
//        signatureFile.delete()
//        signatureFile << result.digest("SHA-512")
//        def testCoverageResult = getTestCoverageResult(testCoverageReportXmlFullPath)
//        println "\ttest coverage result: " + (100*testCoverageResult).toLong().toString() + "%"
//    }
//}

tasks.create("runTestCoverageVerification") {
    dependsOn(
        subprojects.flatMap {
            it.tasks
        }.filter {
            it.name.startsWith("jacocoTestCoverageVerification")
        }
    )
}
//task runTestCoverageVerification {
//    dependsOn subprojects.tasks.collectMany { it }.findAll {
//        it.name.startsWith("jacocoTestCoverageVerification")
//    }
//}

private val repositoryUrl = "https://kepocnhh.github.io/ContinuousStuffExperience"
private val createBadgeUrl: (String, String, String) -> String by ext
private val getTestCoverageResultBadgeColor: (Double) -> String by ext

fun getTestCoverageBadge(signaturePath: String, reportXmlPath: String): String {
    val result = getTestCoverageResult(reportXmlPath)
    val badgeUrl = createBadgeUrl(
        "test_coverage",
        (100*result).toLong().toString() + "%25",
        getTestCoverageResultBadgeColor(result)
    )
    val hash = file(signaturePath).readText()
    if(hash.isEmpty()) {
        throw IllegalStateException("Test coverage signature must not be empty!")
    }
    val reportUrl = "$repositoryUrl/reports/coverage/$hash"
    return "[![test coverage]($badgeUrl)]($reportUrl)"
}

private val getTestingResult: (String) -> Boolean by ext
private val getTestingResultBadgeColor: (Boolean) -> String by ext

fun getTestingBadge(signaturePath: String, reportHtmlPath: String): String {
    val isPassed = getTestingResult(reportHtmlPath)
    val resultText = if(isPassed) {
        "passed"
    } else {
        "failed"
    }
    val badgeUrl = createBadgeUrl(
        "testing",
        resultText,
        getTestingResultBadgeColor(isPassed)
    )
    val hash = file(signaturePath).readText()
    if(hash.isEmpty()) {
        throw IllegalStateException("Testing signature must not be empty!")
    }
    val reportUrl = "$repositoryUrl/reports/testing/$hash"
    return "[![testing]($badgeUrl)]($reportUrl)"
}

private val createBadgeUrlTitleOnly: (String, String) -> String by ext

fun getDocumentationBadge(signaturePath: String): String {
    val hash = file(signaturePath).readText()
    if(hash.isEmpty()) {
        throw IllegalStateException("Documentation signature must not be empty!")
    }
    val url = "$repositoryUrl/documentation/$hash"
    val badgeUrl = createBadgeUrlTitleOnly(
        "documentation",
        "212121"
    )
    return "[![testing]($badgeUrl)]($url)"
}

tasks.create("checkReadme") {
    doLast {
        val readmeFileName = "README.md"
        val readmeText = file(readmeFileName).readText()
        if(readmeText.isEmpty()) {
            throw IllegalStateException("File $readmeFileName must not be empty!")
        }
        val testCoverageBadge = getTestCoverageBadge(
            "$testCoverageReportPath/signature",
            testCoverageReportXmlFullPath
        )
        if(!readmeText.contains(testCoverageBadge)) {
            throw IllegalStateException(
                "File $readmeFileName must contains test coverage result badge:\n$testCoverageBadge"
            )
        }
        val testingBadge = getTestingBadge(
            "$testingReportPath/signature",
            "$testingReportPath/html/index.html"
        )
        if(!readmeText.contains(testingBadge)) {
            throw IllegalStateException(
                "File $readmeFileName must contains testing result badge:\n$testingBadge"
            )
        }
        val documentationBadge = getDocumentationBadge(
            "$documentationPath/signature"
        )
        if(!readmeText.contains(documentationBadge)) {
            throw IllegalStateException(
                "File $readmeFileName must contains documentation badge:\n$documentationBadge"
            )
        }
    }
}
//task checkReadme() {
//    doLast {
//        def readmeFileName = "README.md"
//        def readmeText = getFileText(readmeFileName)
//        if(readmeText == null || readmeText.isEmpty()) {
//            throw new IllegalStateException("File $readmeFileName must not be empty!")
//        }
//        def testCoverageBadge = getTestCoverageBadge(
//            "$testCoverageReportPath/signature",
//            testCoverageReportXmlFullPath
//        )
//        if(!readmeText.contains(testCoverageBadge)) {
//            throw new IllegalStateException(
//                "File $readmeFileName must contains test coverage result badge:\n$testCoverageBadge"
//            )
//        }
//        def testingBadge = getTestingBadge(
//            "$testingReportPath/signature",
//            "$testingReportPath/html/index.html"
//        )
//        if(!readmeText.contains(testingBadge)) {
//            throw new IllegalStateException(
//                "File $readmeFileName must contains testing result badge:\n$testingBadge"
//            )
//        }
//        def documentationBadge = getDocumentationBadge(
//            "$documentationPath/signature"
//        )
//        if(!readmeText.contains(documentationBadge)) {
//            throw new IllegalStateException(
//                    "File $readmeFileName must contains documentation badge:\n$documentationBadge"
//            )
//        }
//    }
//}