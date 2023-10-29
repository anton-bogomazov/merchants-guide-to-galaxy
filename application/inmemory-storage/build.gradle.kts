dependencies {
    implementation(Libs.arrow)
    implementation(Libs.slf4j_api)
    implementation(Libs.logback)
    implementation(project(":application:common"))
    implementation(project(":application:core"))
    implementation(project(":application:core"))

    implementation(Libs.tomcat)
    implementation(Libs.kotlix_serialization)

    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
    testImplementation(Libs.kotest_datatest)
    testImplementation(Libs.kotest_property)
    testImplementation(Libs.kotest_arrow)

    testImplementation(Libs.mockk)
    testImplementation(testFixtures(project(":application:core")))

    testFixturesImplementation(project(":application:core"))
    testFixturesImplementation(Libs.arrow)
}
