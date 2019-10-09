plugins {
    kotlin("jvm")
    id("jacoco")
}

repositories {
    jcenter()
}

jacoco {
    toolVersion = Version.jacoco
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
    implementation(kotlin(module = "stdlib", version = Version.kotlin))

    testImplementation(
        group = "org.junit.jupiter",
        name = "junit-jupiter-engine",
        version = Version.jupiter
    )
}

tasks.withType(Test::class) {
  useJUnitPlatform()
}