object Libs {
    object Versions {
        const val arrow = "1.2.0"

        const val kotest = "5.7.2"
        const val kotest_arrow = "1.4.0"
        const val mockk = "1.13.8"
    }

    const val arrow = "io.arrow-kt:arrow-core:${Versions.arrow}"

    const val kotest_runner = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    const val kotest_assertions = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    const val kotest_property = "io.kotest:kotest-property:${Versions.kotest}"
    const val kotest_datatest = "io.kotest:kotest-framework-datatest:${Versions.kotest}"

    const val kotest_arrow = "io.kotest.extensions:kotest-assertions-arrow:${Versions.kotest_arrow}"

    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}
