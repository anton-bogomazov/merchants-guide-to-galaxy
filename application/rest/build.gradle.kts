plugins {
    kotlin(Plugins.kotlin_serialization) version
            Plugins.Versions.kotlin_serialization
}

dependencies {
    implementation(Libs.arrow)
    implementation(Libs.slf4j_api)
    implementation(project(":application:common"))
    implementation(project(":application:core"))

    implementation(Libs.tomcat)
    implementation(Libs.kotlix_serialization)

    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
    testImplementation(Libs.kotest_arrow)
    // TODO share in common module
    testFixturesImplementation(testFixtures(project(":application:cli")))
    testFixturesImplementation(testFixtures(project(":application:core")))
    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.mockk)

    testImplementation(Libs.mockk)
    testImplementation(testFixtures(project(":application:core")))
}
