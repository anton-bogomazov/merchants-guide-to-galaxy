plugins {
    application
}

dependencies {
    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
    testImplementation(Libs.kotest_property)

    testImplementation(Libs.mockk)
}

application {
    mainClass.set("com.abogomazov.merchant.guide.MainKt")
}
