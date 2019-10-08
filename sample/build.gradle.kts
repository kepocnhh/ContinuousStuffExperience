plugins {
    application
    kotlin("jvm")
}

repositories {
    jcenter()
}

val kotlinVersion: String by rootProject.ext

application {
    mainClassName = "continuous.stuff.experience.AppKt"
}

dependencies {
    implementation(project(":lib"))
    implementation(project(":lib2"))
    implementation(kotlin(module = "stdlib", version = kotlinVersion))
}
