package com.abogomazov.merchant.guide.storage.postgres

import com.abogomazov.merchant.guide.storage.postgres.repository.MarketPostgresRepository
import com.abogomazov.merchant.guide.storage.postgres.repository.TranslationPostgresRepository
import org.testcontainers.containers.PostgreSQLContainer

fun postgresDatasource(container: PostgreSQLContainer<*>) =
    postgresDatasource(container.getJdbcUrl(), container.username, container.password)

fun marketRepository(container: PostgreSQLContainer<*>) =
    MarketPostgresRepository(postgresDatasource(container))

fun translationRepository(container: PostgreSQLContainer<*>) =
    TranslationPostgresRepository(postgresDatasource(container))

fun postgresDatasource(
    jdbcUrl: String,
    username: String,
    password: String
) =
    PostgresDatasource(
        hikariDataSource(
            jdbcUrl = jdbcUrl,
            username = username,
            password = password
        )
    )
