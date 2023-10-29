dependencies {
    implementation(Libs.postgres_driver)
    implementation(Libs.hikari_pool)
    implementation(Libs.liquibase)

    implementation(Libs.arrow)
    implementation(project(":application:core"))

    testImplementation(Libs.testcontainers)
    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
    testImplementation(Libs.kotest_arrow)

    testImplementation(testFixtures(project(":application:core")))

    testFixturesImplementation(project(":application:core"))
    testFixturesImplementation(Libs.testcontainers)
    testFixturesImplementation(Libs.hikari_pool)
    testFixturesImplementation(Libs.arrow)
    testFixturesImplementation(Libs.kotest_runner)
    testFixturesImplementation(Libs.kotest_arrow)
}
