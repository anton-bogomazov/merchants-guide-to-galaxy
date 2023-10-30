package com.abogomazov.merchant.guide.application.property

import java.util.*

private const val APPLICATION_PROPERTIES = "application"
private const val APPLICATION_UI = "$APPLICATION_PROPERTIES.ui"
private const val APPLICATION_STORAGE = "$APPLICATION_PROPERTIES.storage"

private const val DB_PROPERTIES = "postgres"
private const val POSTGRES_URL = "$DB_PROPERTIES.jdbcUrl"
private const val POSTGRES_USER = "$DB_PROPERTIES.username"
private const val POSTGRES_PASS = "$DB_PROPERTIES.password"

private val DEFAULT_UI_VALUE = UI.CLI
private val DEFAULT_STORAGE_VALUE = Storage.INMEMORY

class ApplicationProperties(
    private val profile: String
) {

    val application = properties(APPLICATION_PROPERTIES)?.let {
        val uiProp = it.resolve(APPLICATION_UI, DEFAULT_UI_VALUE.toString())
        val storageProp = it.resolve(APPLICATION_STORAGE, DEFAULT_STORAGE_VALUE.toString())

        ApplicationProperty(
            ui = UI.valueOf(uiProp.uppercase()),
            storage = Storage.valueOf(storageProp.uppercase()),
        )
    } ?: defaultProperties()

    val db by lazy {
        properties(DB_PROPERTIES)?.let {
            PostgresProperty(
                jdbcUrl = it.getOrThrow(POSTGRES_URL),
                username = it.getOrThrow(POSTGRES_USER),
                password = it.getOrThrow(POSTGRES_PASS),
            )
        } ?: propertiesRequiredError(DB_PROPERTIES)
    }

    private fun properties(name: String): Properties? =
        loadPropertyFile(propertyFilename(name))?.let { stream ->
            Properties().apply { this.load(stream) }
        }

    private fun loadPropertyFile(path: String) =
        ApplicationProperties::class.java.getResourceAsStream(path)

    private fun propertyFilename(propName: String) = "/$propName-$profile.properties"

    private fun propertiesRequiredError(propertiesName: String): Nothing =
        error("$propertiesName-$profile.properties is required")

    private fun defaultProperties() =
        ApplicationProperty(
            ui = UI.CLI,
            storage = Storage.INMEMORY,
        )
}

private fun Properties.getOrThrow(propertyName: String) =
    this.getProperty(propertyName) ?: propertyLoadingError(propertyName)
private fun Properties.resolve(propertyName: String, defaultValue: String) =
    this.getProperty(propertyName) ?: defaultValue

private fun propertyLoadingError(propertyName: String): Nothing =
    error("Cannot load property $propertyName")
