data class Dependency(
    val group: String,
    val name: String,
    val version: String
) {
    companion object {
        val kotlinGradlePlugin = Dependency(
            group = "org.jetbrains.kotlin",
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
            group = "org.jetbrains.kotlin",
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
            name = "org.jetbrains.dokka",
            version = Version.documentation
        )
        val testCoverage = Plugin(
            name = "org.gradle.jacoco",
            version = Version.testCoverage
        )
    }
}
