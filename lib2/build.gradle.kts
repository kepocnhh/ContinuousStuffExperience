plugins {
    kotlin("jvm")
    id("jacoco")
}

repositories {
    jcenter()
}

val kotlinVersion = rootProject.ext["kotlinVersion"] as String
val jacocoVersion = rootProject.ext["jacocoVersion"] as String
val jupiterVersion = rootProject.ext["jupiterVersion"] as String

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

tasks.withType(Test::class) {
  useJUnitPlatform()
}