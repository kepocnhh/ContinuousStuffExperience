import analysis.analysisQualityHtmlPath
import analysis.analysisStyleHtmlPath
import coverage.*
import documentation.documentationHtmlPath
import documentation.rewriteDocumentationSignature
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import readme.createFixReadmeTask
import readme.createVerifyReadmeTask
import testing.createCollectTestingReportTask
import testing.createRunTestTask
import util.*
import version.createIncrementVersionPatchTask
import version.createIncrementVersionMinorTask

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath(Dependency.kotlinGradlePlugin.notation())
    }
}

plugins {
    id(Plugin.testCoverage.name)
    id(Plugin.analysis.name) version Plugin.analysis.version
    id(Plugin.documentation.name) version Plugin.documentation.version
}

repositories {
    jcenter()
}

version = versionName()

val kotlinLint: Configuration by configurations.creating

dependencies {
    kotlinLint(Dependency.kotlinLint.notation())
    kotlinLint(Dependency.kotlinLintHtmlReporter.notation())
}

task<JavaExec>("verifyStyle") {
    classpath = kotlinLint
    main = "com.pinterest.ktlint.Main"
    args(
        "**/src/**/*.kt",
        "--reporter=html," +
            "artifact=${Dependency.kotlinLintHtmlReporter.notation()}," +
            "output=$analysisStyleHtmlPath"
    )
}

evaluationDependsOnChildren()

task<DefaultTask>("version") {
    val dependsOnTasks = mutableListOf<DefaultTask>()
    subprojects.forEach { project ->
        project.task<DefaultTask>(name) {
            dependsOnTasks.add(this)
            doLast {
                println("${project.name}:version: ${project.version}")
            }
        }
    }
    dependsOn(dependsOnTasks)
    doLast {
        println("${project.name}:version: $version")
    }
}

createIncrementVersionPatchTask()
subprojects.withPropertiesNotEmpty(
    "versionPatch"
).forEach {
    it.createIncrementVersionPatchTask()
}

createIncrementVersionMinorTask()
subprojects.withPropertiesNotEmpty(
    "versionMinor",
    "versionPatch"
).forEach {
    it.createIncrementVersionMinorTask()
}

task<DokkaTask>("collectDocumentation") {
    outputFormat = "html"
    outputDirectory = documentationHtmlPath
    sourceDirs = subprojects.withPlugin("kotlin").srcDirs("main")
    doLast {
        rewriteDocumentationSignature()
    }
}

jacoco {
    toolVersion = Version.testCoverage
}

detekt {
    toolVersion = Version.analysis
}

task<Detekt>("verifyDocumentation") {
    source = files(subprojects.withPlugin("kotlin").srcDirs("main")).asFileTree
    config = files("buildSrc/src/main/resources/detekt/config/documentation.yml")
    reports {
        html.enabled = false
        xml.enabled = false
        txt.enabled = false
    }
}

task<Detekt>("verifyQuality") {
    //comments
    //todo complexity
    //todo empty-blocks
    //todo exceptions
    //todo formatting
    //todo naming
    //todo performance
    //todo potential-bugs
    //todo style
    source = files(subprojects.withPlugin("kotlin").srcDirs("main")).asFileTree
    val configPath = "buildSrc/src/main/resources/detekt/config"
    config.setFrom(
        "$configPath/common.yml",
//        "$configPath/comments.yml"
//        "$configPath/complexity.yml"
        "$configPath/empty_blocks.yml"
//        "$configPath/potential_bugs.yml"
//        "$configPath/performance.yml"
    )//todo https://github.com/arturbosch/detekt/issues/2045
    reports {
        html {
            enabled = true
            destination = File(analysisQualityHtmlPath)
        }
        xml.enabled = false
        txt.enabled = false
    }
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}

task<DefaultTask>("verifyWarning") {
    val dependsOnTasks = mutableListOf<KotlinCompile>()
    subprojects.withPlugin("kotlin").forEach { project ->
        project.task<KotlinCompile>(name) {
            AbstractKotlinCompile::class.java.methods.single {
                it.name == "setSourceSetName\$kotlin_gradle_plugin"
            }.invoke(this, "main")
            outputs.upToDateWhen { false }
            val tasks = project.tasks.withType(AbstractKotlinCompile::class.java).filter { it != this }
            dependsOn(tasks)
            source = files(tasks.source()).asFileTree
            destinationDir = File(project.buildDir.absolutePath + "/kotlin/$name")
            classpath = files(tasks.classpath())
            kotlinOptions.allWarningsAsErrors = true
            dependsOnTasks.add(this)
        }
    }
    dependsOn(dependsOnTasks)
}

task<DefaultTask>("compile") {
    dependsOn("clean")
    dependsOn(
        subprojects.tasksBy {
            it.name.startsWith("compile")
        }
    )
}

createCollectTestingReportTask()

createRunTestTask()

createCollectTestCoverageReportTask()

task<DefaultTask>("verifyTestCoverage") {
    dependsOn(subprojects.tasksWithType<JacocoCoverageVerification>())
}

createVerifyReadmeTask()
createFixReadmeTask()
