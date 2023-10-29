package com.abogomazov.merchant.guide.storage.postgres

import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.BeforeTestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.testcontainers.containers.PostgreSQLContainer

object PostgresContainer : BeforeTestListener, AfterTestListener {
    val container = PostgreSQLContainer("postgres:16-alpine")
    override suspend fun beforeTest(testCase: TestCase) {
        println("Starting a postgres container for ${testCase.descriptor.id}")
        container.start()
    }
    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        container.stop()
        println("Postgres container stopped for ${testCase.descriptor.id}")
    }
}
