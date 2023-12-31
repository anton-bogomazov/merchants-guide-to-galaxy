object Libs {
    object Versions {
        const val arrow = "1.2.0"
        const val slf4j = "2.0.9"
        const val logback = "1.4.11"

        const val tomcat = "10.1.15"
        const val kotlix_serialization = "1.6.0"

        const val postgres_driver = "42.6.0"
        const val hikari_pool = "5.0.1"
        const val liquibase = "4.24.0"

        const val testcontainers = "1.19.1"

        const val kotest = "5.7.2"
        const val kotest_arrow = "1.4.0"
        const val mockk = "1.13.8"
    }

    const val arrow = "io.arrow-kt:arrow-core:${Versions.arrow}"
    const val slf4j_api = "org.slf4j:slf4j-api:${Versions.slf4j}"
    const val logback = "ch.qos.logback:logback-classic:${Versions.logback}"

    const val tomcat = "org.apache.tomcat.embed:tomcat-embed-core:${Versions.tomcat}"
    const val kotlix_serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:${Versions.kotlix_serialization}"

    const val liquibase = "org.liquibase:liquibase-core:${Versions.liquibase}"

    const val postgres_driver = "org.postgresql:postgresql:${Versions.postgres_driver}"
    const val hikari_pool = "com.zaxxer:HikariCP:${Versions.hikari_pool}"

    const val testcontainers = "org.testcontainers:postgresql:${Versions.testcontainers}"

    const val kotest_runner = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    const val kotest_assertions = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    const val kotest_property = "io.kotest:kotest-property:${Versions.kotest}"
    const val kotest_datatest = "io.kotest:kotest-framework-datatest:${Versions.kotest}"

    const val kotest_arrow = "io.kotest.extensions:kotest-assertions-arrow:${Versions.kotest_arrow}"

    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}
