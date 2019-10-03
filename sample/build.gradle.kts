plugins {
    application
    kotlin("jvm")
}

repositories {
    jcenter()
}

val kotlinVersion = rootProject.ext["kotlinVersion"] as String

application {
    mainClassName = "continuous.stuff.experience.AppKt"
}

dependencies {
    implementation(project(":lib"))
    implementation(project(":lib2"))
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}
