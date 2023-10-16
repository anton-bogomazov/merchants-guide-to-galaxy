import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
    id(Plugins.detekt) version(Plugins.Versions.detekt)
}

group = "com.abogomazov"
version = "1.0-SNAPSHOT"

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("org.jetbrains.kotlin.jvm")

        plugin(Plugins.detekt)
        plugin("jacoco")
    }

    detekt {
        toolVersion = Plugins.Versions.detekt
        config.setFrom(file("../detekt/detekt.yml"))
        buildUponDefaultConfig = true

        dependencies {
            detektPlugins(Plugins.detekt_formatting)
        }
    }

    tasks {
        withType<KotlinCompile> {
            val failOnWarning = project.properties["allWarningsAsErrors"] != null
                    && project.properties["allWarningsAsErrors"] == "true"
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
                allWarningsAsErrors = failOnWarning
                freeCompilerArgs = listOf("-Xjvm-default=all")
            }
        }

        withType<JavaCompile> {
            options.compilerArgs.add("-Xlint:all")
        }

        withType<Test> {
            useJUnitPlatform()
            finalizedBy(named<JacocoReport>("jacocoTestReport"))
        }
    }
}
