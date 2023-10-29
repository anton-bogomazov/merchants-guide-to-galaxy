package com.abogomazov.merchant.guide.storage.postgres

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.ResultSet
import javax.sql.DataSource

class PostgresDatasource(
    jdbcUrl: String,
    username: String,
    password: String,
) {

    private var datasource: DataSource = hikariDataSource(jdbcUrl, username, password)

    fun connection(): Connection = datasource.connection

    fun <R> single(sql: String, callback: (ResultSet) -> R): R? {
        val result = multiple(sql, callback)
        if (result.size > 1) error("${result.size} results provided")
        return multiple(sql, callback).singleOrNull()
    }

    fun <R> multiple(sql: String, callback: (ResultSet) -> R): List<R> {
        return execute(sql) {
            val results = mutableListOf<R>()
            while (it.next()) {
                results.add(callback(it))
            }
            results
        } ?: emptyList()
    }

    private fun <R> execute(sql: String, mapper: (ResultSet) -> R): R? {
        return datasource.connection.use { connection ->
            connection.prepareStatement(sql).use {
                it.executeQuery().let(mapper)
            }
        }
    }

    fun update(sql: String) {
        datasource.connection.use { connection ->
            connection.prepareStatement(sql).use {
                it.executeUpdate()
            }
        }
    }
}

fun hikariDataSource(
    jdbcUrl: String,
    username: String,
    password: String,
) = HikariDataSource()
    .apply {
        this.jdbcUrl = jdbcUrl
        this.username = username
        this.password = password
    }
