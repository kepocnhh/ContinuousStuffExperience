import org.gradle.api.Project
import util.requirePropertyNotEmpty

object Version {
    const val kotlin = "1.3.50"
    const val analysis = "1.1.1" // detekt
    const val testCoverage = "0.8.4" // jacoco
    const val testing = "5.0.1" // jupiter
    const val documentation = "0.9.18" // dokka
    const val kotlinLint = "0.34.2"
    const val kotlinLintHtmlReporter = "0.2.3"
}

const val VERSION_PATCH_KEY = "versionPatch"
const val VERSION_MINOR_KEY = "versionMinor"
const val VERSION_MAJOR_KEY = "versionMajor"

fun Project.versionName(): String {
    val versionMajor = requirePropertyNotEmpty(VERSION_MAJOR_KEY)
    val versionMinor = requirePropertyNotEmpty(VERSION_MINOR_KEY)
    val versionPatch = requirePropertyNotEmpty(VERSION_PATCH_KEY)
    return "$versionMajor.$versionMinor.$versionPatch"
}

fun Project.versionPatch(): Int {
    return requirePropertyNotEmpty(VERSION_PATCH_KEY).toInt()
}

fun Project.versionMinor(): Int {
    return requirePropertyNotEmpty(VERSION_MINOR_KEY).toInt()
}
