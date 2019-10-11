package documentation

import analysisDocumentationPath
import org.gradle.api.Project

val Project.analysisDocumentationReportXmlPath get() = "$analysisDocumentationPath/xml/report.xml"