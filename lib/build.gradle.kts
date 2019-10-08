plugins {
    kotlin("jvm")
    id("jacoco")
}

repositories {
    jcenter()
}

val kotlinVersion: String by rootProject.ext
val jacocoVersion: String by rootProject.ext
val jupiterVersion: String by rootProject.ext

jacoco {
    toolVersion = jacocoVersion
}

tasks.withType(JacocoReport::class) {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
        csv.isEnabled = false
    }
}

tasks.withType(JacocoCoverageVerification::class) {
  violationRules {
    rule {
      limit {
        counter = "LINE"
        value = "COVEREDRATIO"
        minimum = BigDecimal(1.0)
      }
    }
  }
}

dependencies {
    implementation(kotlin(module = "stdlib", version = kotlinVersion))

    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

tasks.withType(Test::class) {
  useJUnitPlatform()
}