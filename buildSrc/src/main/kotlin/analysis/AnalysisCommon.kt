package analysis

import analysisPath
import org.gradle.api.Project

val Project.analysisStylePath get() = "$analysisPath/style"
val Project.analysisStyleXmlPath get() = "$analysisStylePath/xml/report.xml"
val Project.analysisStyleHtmlPath get() = "$analysisStylePath/html/report.html"

val Project.analysisQualityPath get() = "$analysisPath/quality"
val Project.analysisQualityHtmlPath get() = "$analysisQualityPath/html/report.html"
