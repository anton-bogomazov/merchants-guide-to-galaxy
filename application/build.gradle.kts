plugins {
    application
}

dependencies {
    implementation(Libs.arrow)

    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
    testImplementation(Libs.kotest_property)
    testImplementation(Libs.kotest_arrow)

    testImplementation(Libs.mockk)
}

application {
    mainClass.set("com.abogomazov.merchant.guide.MainKt")
}
