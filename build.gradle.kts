import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.kotlin_jvm) version Core.Versions.KOTLIN_VERSION apply false
    id(Plugins.detekt) version(Plugins.Versions.detekt)
}

group = "com.abogomazov"
version = "1.0-SNAPSHOT"

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin(Plugins.kotlin_jvm)

        plugin(Plugins.detekt)
        plugin("jacoco")
    }

    detekt {
        toolVersion = Plugins.Versions.detekt
        config.setFrom(file(Plugins.Paths.detekt_config))
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
                jvmTarget = Core.Versions.JAVA_VERSION
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
