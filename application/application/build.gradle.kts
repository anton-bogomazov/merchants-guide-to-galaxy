plugins {
    application
}

kotlin {
    jvmToolchain(JavaLanguageVersion.of(Core.Versions.JAVA_VERSION).asInt())
}

dependencies {
    implementation(Libs.slf4j_api)
    implementation(Libs.logback)
    implementation(project(":application:core"))
    implementation(project(":application:cli"))
    implementation(project(":application:rest"))
    implementation(project(":application:common"))
    implementation(project(":application:inmemory-storage"))
    implementation(project(":application:postgres-storage"))

    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
    testImplementation(Libs.kotest_property)

    testFixturesImplementation(project(":application:core"))
    testFixturesImplementation(project(":application:common"))
    testFixturesImplementation(project(":application:cli"))
    testFixturesImplementation(project(":application:inmemory-storage"))
    testFixturesImplementation(Libs.kotest_assertions)
}

application {
    mainClass.set("com.abogomazov.merchant.guide.MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
