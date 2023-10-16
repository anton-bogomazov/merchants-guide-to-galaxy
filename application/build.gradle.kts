plugins {
    application
}

dependencies {
    testImplementation(Libs.kotest_runner)
    testImplementation(Libs.kotest_assertions)
}

application {
    mainClass.set("com.abogomazov.merchant.guide.MainKt")
}
