plugins {
    application
    kotlin("jvm")
}

repositories {
    jcenter()
}

application {
    mainClassName = "continuous.stuff.experience.AppKt"
}

dependencies {
    implementation(project(":lib"))
    implementation(project(":lib2"))
    implementation(kotlin(module = "stdlib", version = Version.kotlin))
}
