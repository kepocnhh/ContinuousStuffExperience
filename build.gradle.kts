import analysis.analysisStyleHtmlPath
import coverage.*
import documentation.documentationHtmlPath
import documentation.rewriteDocumentationSignature
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import readme.createCheckReadmeTask
import testing.createCollectTestingReportTask
import testing.createRunTestsTask
import util.*

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

val kotlinLint: Configuration by configurations.creating

dependencies {
    kotlinLint(Dependency.kotlinLint.notation())
    kotlinLint(Dependency.kotlinLintHtmlReporter.notation())
}

task<JavaExec>("runStyleVerification") {
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

task<Detekt>("runDocumentationVerification") {
    source = files(subprojects.withPlugin("kotlin").srcDirs("main")).asFileTree
    config = files("buildSrc/src/main/resources/detekt/config/documentation.yml")
    reports {
        html.enabled = false
        xml.enabled = false
        txt.enabled = false
    }
}

task<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}

subprojects.withPlugin("kotlin").forEach { project ->
    project.task<KotlinCompile>("checkWarning") {
        AbstractKotlinCompile::class.java.methods.single {
            it.name == "setSourceSetName\$kotlin_gradle_plugin"
        }.invoke(this, "main")
        outputs.upToDateWhen { false }
        val tasks = project.tasks.withType(AbstractKotlinCompile::class.java).filter { it != this }
        dependsOn(tasks)
        source = files(tasks.source()).asFileTree
        destinationDir = File(project.buildDir.absolutePath + "/kotlin/checkWarning")
        classpath = files(tasks.classpath())
        kotlinOptions.allWarningsAsErrors = true
    }
}

task<DefaultTask>("checkWarning") {
    val tasks = subprojects.tasksWithType<KotlinCompile>().filterByName("checkWarning")
    dependsOn(tasks)
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

createRunTestsTask()

createCollectTestCoverageReportTask()

task<DefaultTask>("runTestCoverageVerification") {
    dependsOn(subprojects.tasksWithType<JacocoCoverageVerification>())
}

createCheckReadmeTask()
