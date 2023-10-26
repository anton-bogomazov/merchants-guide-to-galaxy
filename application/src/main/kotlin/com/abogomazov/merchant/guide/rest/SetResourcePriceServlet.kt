package com.abogomazov.merchant.guide.rest

import arrow.core.raise.either
import com.abogomazov.merchant.guide.cli.parser.toCredit
import com.abogomazov.merchant.guide.cli.parser.toGalaxyNumber
import com.abogomazov.merchant.guide.cli.parser.toResource
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SetResourcePriceServlet(
    private val setResourcePriceUseCase: SetResourceMarketPriceUseCase
) : HttpServlet() {
    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val dto = req?.reader?.use {
            Json.decodeFromString<SetResourcePriceDto>(it.readText())
        }

        dto?.let {
            either {
                dto.resource.toResource().bind() to dto.totalPrice.toCredit().bind()
            }.map { (resource, credits) ->
                setResourcePriceUseCase.execute(
                    totalResourceAmount = dto.totalResourceAmount.toGalaxyNumber(),
                    resource = resource,
                    totalPrice = credits
                )
            }
        } ?: resp?.apply {
            status = HttpServletResponse.SC_BAD_REQUEST
        }
    }
}

@Serializable
private data class SetResourcePriceDto(
    val totalResourceAmount: String,
    val resource: String,
    val totalPrice: String,
)
