plugins {
    kotlin("jvm") version "1.9.0"
    application

    id("io.gitlab.arturbosch.detekt") version("1.23.1")
}

group = "com.abogomazov"
version = "1.0-SNAPSHOT"

detekt {
    toolVersion = "1.23.1"
    config.setFrom(file("detekt/detekt.yml"))
    buildUponDefaultConfig = true

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("com.abogomazov.merchant.guide.MainKt")
}
