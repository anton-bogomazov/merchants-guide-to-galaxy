package com.abogomazov.merchant.guide.rest.servlet

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumber
import com.abogomazov.merchant.guide.domain.market.toCredit
import com.abogomazov.merchant.guide.domain.market.toResource
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SetResourcePriceServlet(
    private val setResourcePriceUseCase: SetResourceMarketPriceUseCase
) : HttpServlet() {

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val dto = parse(req)

        resp.writer.use { writer ->
            val response = execute(dto)
            writer.write(Json.encodeToString(response))
            resp.status = response.code
            writer.flush()
        }
    }

    private fun parse(req: HttpServletRequest) =
        req.reader.use {
            Json.decodeFromString<SetResourcePriceDto>(it.readText())
        }

    private fun execute(dto: SetResourcePriceDto): Response =
        either {
            Triple(
                dto.resource.toResource().bind(),
                dto.amount.toGalaxyNumber().bind(),
                dto.cost.toCredit().bind()
            )
        }.flatMap { (resource, totalResourceAmount, credits) ->
            setResourcePriceUseCase.execute(
                resource = resource,
                resourceQuantity = totalResourceAmount,
                resourceCost = credits
            ).map { "OK" }
        }.toResponse()
}

@Serializable
data class SetResourcePriceDto(
    val amount: String,
    val resource: String,
    val cost: String,
)

fun <E, R> Either<E, R>.toResponse() =
    this.fold(
        { err -> Response(err.toString(), HttpServletResponse.SC_BAD_REQUEST) },
        { result -> Response(result.toString(), HttpServletResponse.SC_OK) }
    )
