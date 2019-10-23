plugins {
    application
    id(Plugin.kotlinJvm.name)
}

repositories {
    jcenter()
}

version = versionName()

application {
    mainClassName = "continuous.stuff.experience.AppKt"
}

dependencies {
    implementation(project(":lib"))
    implementation(project(":lib2"))
    implementation(Dependency.kotlinStdlib.notation())
}
