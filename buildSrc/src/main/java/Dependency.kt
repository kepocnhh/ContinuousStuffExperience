import org.gradle.api.Project

object Version {
    const val kotlin = "1.3.50"
    const val detekt = "1.0.1"
    const val jacoco = "0.8.4"
    const val jupiter = "5.0.1"
    const val dokka = "0.9.18"
}

const val repositoryUrl = "https://kepocnhh.github.io/ContinuousStuffExperience"

val Project.documentationPath get() = "${rootProject.buildDir}/documentation"

val Project.reportsPath get() = "${rootProject.buildDir}/reports"

val Project.analysisPath get() = "$reportsPath/analysis"
val Project.analysisDocumentationPath get() = "$analysisPath/documentation"

val Project.testCoverageReportPath get() = "$reportsPath/coverage"

val Project.testingReportPath get() = "$reportsPath/testing"