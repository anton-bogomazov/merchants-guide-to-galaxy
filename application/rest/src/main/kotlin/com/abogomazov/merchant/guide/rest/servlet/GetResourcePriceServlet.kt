package com.abogomazov.merchant.guide.rest.servlet

import arrow.core.flatMap
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumber
import com.abogomazov.merchant.guide.domain.market.toResource
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GetResourcePriceServlet(
    private val getResourcePriceUseCase: GetResourceMarketPriceUseCase
) : HttpServlet() {

    companion object {
        private const val RESOURCE = "resource"
        private const val AMOUNT = "amount"
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val (resource, amount) = parse(req)
        resp.writer.use { writer ->
            val response = execute(amount, resource)
            writer.write(Json.encodeToString(response))
            resp.status = response.code
            writer.flush()
        }
    }

    private fun parse(req: HttpServletRequest) =
        req.getParameter(RESOURCE) to req.getParameter(AMOUNT)

    private fun execute(amountOfResource: String, resource: String): Response =
        either { resource.toResource().bind() to amountOfResource.toGalaxyNumber().bind() }
            .flatMap { (resource, localNumber) ->
                getResourcePriceUseCase.execute(localNumber, resource)
                    .map { it.toBigInteger().intValueExact().toString() }
            }.toResponse()
}
