package com.abogomazov.merchant.guide.storage.postgres

import com.abogomazov.merchant.guide.storage.postgres.repository.MarketRepository
import com.abogomazov.merchant.guide.storage.postgres.repository.TranslationRepository
import org.testcontainers.containers.PostgreSQLContainer

fun postgresDatasource(container: PostgreSQLContainer<*>) =
    postgresDatasource(container.getJdbcUrl(), container.username, container.password)

fun marketRepository(container: PostgreSQLContainer<*>) =
    MarketRepository(postgresDatasource(container))

fun translationRepository(container: PostgreSQLContainer<*>) =
    TranslationRepository(postgresDatasource(container))

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
