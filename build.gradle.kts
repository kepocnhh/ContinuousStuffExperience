import readme.createCheckReadmeTask
import coverage.*
import documentation.analysisDocumentationReportXmlPath
import documentation.documentationHtmlPath
import documentation.rewriteDocumentationSignature
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.dokka.gradle.DokkaTask
import testing.createCollectTestingReportTask
import testing.createRunTestsTask
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
    jacoco
    id("io.gitlab.arturbosch.detekt") version Version.detekt
    id("org.jetbrains.dokka") version Version.dokka
}

repositories {
    jcenter()
}

evaluationDependsOnChildren()

tasks.create<DokkaTask>("collectDocumentation") {
    outputFormat = "html"
    outputDirectory = documentationHtmlPath
    sourceDirs = subprojects.withPlugin("kotlin").srcDirs("main")
    doLast {
        rewriteDocumentationSignature()
    }
}

jacoco {
    toolVersion = Version.jacoco
}

detekt {
    toolVersion = Version.detekt
}

tasks.create<Detekt>("runDocumentationVerification") {
    setSource(subprojects.withPlugin("kotlin").srcDirs("main"))
    config = files("project/detekt/config/documentation.yml")
    reports {
        html.enabled = false
        xml {
            enabled = true
            destination = File(analysisDocumentationReportXmlPath)
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

createCollectTestingReportTask()

createRunTestsTask()

createCollectTestCoverageReportTask()

tasks.create("runTestCoverageVerification") {
    dependsOn(subprojects.tasksWithType<JacocoCoverageVerification>())
}

createCheckReadmeTask()