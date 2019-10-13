import org.gradle.api.Project

val Project.documentationPath get() = "${rootProject.buildDir}/documentation"

val Project.reportsPath get() = "${rootProject.buildDir}/reports"

val Project.analysisPath get() = "$reportsPath/analysis"

val Project.testCoverageReportPath get() = "$reportsPath/coverage"

val Project.testingReportPath get() = "$reportsPath/testing"