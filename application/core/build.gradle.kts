dependencies {
    implementation(Libs.arrow)
    implementation(Libs.slf4j_api)
    implementation(Libs.logback)

    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
    testImplementation(Libs.kotest_datatest)
    testImplementation(Libs.kotest_property)
    testImplementation(Libs.kotest_arrow)
    testImplementation(Libs.mockk)

    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.kotest_runner)
    testFixturesImplementation(Libs.kotest_arrow)
    testFixturesImplementation(Libs.kotest_property)
}
