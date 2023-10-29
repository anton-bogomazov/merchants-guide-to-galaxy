package com.abogomazov.merchant.guide.rest

import com.abogomazov.merchant.guide.rest.servlet.Response
import com.abogomazov.merchant.guide.rest.servlet.SetResourcePriceDto
import com.abogomazov.merchant.guide.rest.servlet.SetTranslationDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class MerchantRestClient {

    companion object {
        private const val HOST = "localhost"
        private const val PORT = 8080
        private const val BASE_URL = "http://$HOST:$PORT"

        private const val GALAXY_PARAMETER = "galaxy"
        private const val RESOURCE_PARAMETER = "resource"
        private const val AMOUNT_PARAMETER = "amount"
    }

    val client: HttpClient = HttpClient
        .newBuilder()
        .build()

    fun getTranslation(galaxyNumber: List<String>): Response {
        val parameter = "$GALAXY_PARAMETER=${galaxyNumber.joinToString("+")}"
        val uri = URI("$BASE_URL/$GET_TRANSLATION?$parameter")
        val request = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build()

        return client.execute(request)
    }

    fun setTranslation(galaxyNumeral: String, romanNumeral: String): Response {
        val dto = SetTranslationDto(
            galaxy = galaxyNumeral,
            roman = romanNumeral
        )
        val json = Json.encodeToString(dto)
        val body = HttpRequest.BodyPublishers.ofString(json)
        val request = HttpRequest.newBuilder()
            .uri(URI("$BASE_URL/$ADD_TRANSLATION"))
            .POST(body)
            .build()

        return client.execute(request)
    }

    fun getResourcePrice(galaxyAmount: List<String>, resource: String): Response {
        val parameters = "$AMOUNT_PARAMETER=${galaxyAmount.joinToString("+")}&$RESOURCE_PARAMETER=$resource"
        val uri = URI("$BASE_URL/$GET_RESOURCE_PRICE?$parameters")
        val request = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build()

        return client.execute(request)
    }

    fun setResourcePrice(galaxyAmount: List<String>, resource: String, cost: String): Response {
        val dto = SetResourcePriceDto(
            amount = galaxyAmount.joinToString(" "),
            resource = resource,
            cost = cost
        )
        val json = Json.encodeToString(dto)
        val body = HttpRequest.BodyPublishers.ofString(json)
        val request = HttpRequest.newBuilder()
            .uri(URI("$BASE_URL/$ADD_RESOURCE_PRICE"))
            .POST(body)
            .build()

        return client.execute(request)
    }

    private fun HttpClient.execute(request: HttpRequest): Response {
        return Json.decodeFromString(this.send(request, HttpResponse.BodyHandlers.ofString()).body())
    }
}
