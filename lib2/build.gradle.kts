plugins {
    kotlin("jvm")
    id(Plugin.testCoverage.name)
}

repositories {
    jcenter()
}

jacoco {
    toolVersion = Version.testCoverage
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
                minimum = BigDecimal(1.0)
            }
        }
    }
}

dependencies {
    implementation(Dependency.kotlinStdlib.notation())

    testImplementation(Dependency.testing.notation())
}

tasks.withType(Test::class) {
  useJUnitPlatform()
}