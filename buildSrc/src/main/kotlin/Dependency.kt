private const val jetbrainsGroup = "org.jetbrains"
private const val kotlinGroup = "$jetbrainsGroup.kotlin"

data class Dependency(
    val group: String,
    val name: String,
    val version: String
) {
    companion object {
        val kotlinGradlePlugin = Dependency(
            group = kotlinGroup,
            name = "kotlin-gradle-plugin",
            version = Version.kotlin
        )
        val kotlinLint = Dependency(
            group = "com.pinterest",
            name = "ktlint",
            version = Version.kotlinLint
        )
        val kotlinLintHtmlReporter = Dependency(
            group = "me.cassiano",
            name = "ktlint-html-reporter",
            version = Version.kotlinLintHtmlReporter
        )
        val kotlinStdlib = Dependency(
            group = kotlinGroup,
            name = "kotlin-stdlib",
            version = Version.kotlin
        )
        val testing = Dependency(
            group = "org.junit.jupiter",
            name = "junit-jupiter-engine",
            version = Version.testing
        )
    }
}

fun Dependency.notation(): String {
    return "$group:$name:$version"
}

data class Plugin(
    val name: String,
    val version: String
) {
    companion object {
        val analysis = Plugin(
            name = "io.gitlab.arturbosch.detekt",
            version = Version.analysis
        )
        val documentation = Plugin(
            name = "$jetbrainsGroup.dokka",
            version = Version.documentation
        )
        val testCoverage = Plugin(
            name = "org.gradle.jacoco",
            version = Version.testCoverage
        )
        val kotlinJvm = Plugin(
            name = "$kotlinGroup.jvm",
            version = Version.kotlin
        )
    }
}
