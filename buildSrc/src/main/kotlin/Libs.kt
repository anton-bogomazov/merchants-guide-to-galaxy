object Libs {
    object Versions {
        const val kotest = "5.7.2"
        const val mockk = "1.13.8"
    }

    const val kotest_runner = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    const val kotest_assertions = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    const val kotest_property = "io.kotest:kotest-property:${Versions.kotest}"

    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}
