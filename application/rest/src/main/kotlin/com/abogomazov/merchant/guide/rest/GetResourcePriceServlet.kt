package com.abogomazov.merchant.guide.rest

import arrow.core.flatMap
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumber
import com.abogomazov.merchant.guide.domain.market.toResource
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

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
            val (response, status) = execute(amount, resource)
            writer.write(response)
            resp.status = status
            writer.flush()
        }
    }

    private fun parse(req: HttpServletRequest) =
        req.getParameter(RESOURCE) to req.getParameter(AMOUNT)

    private fun execute(amountOfResource: String, resource: String): Pair<String, Int> =
        either { resource.toResource().bind() to amountOfResource.toGalaxyNumber().bind() }
            .flatMap { (resource, localNumber) ->
                getResourcePriceUseCase.execute(localNumber, resource)
                    .map { it.toBigInteger().intValueExact().toString() }
            }.fold(
                { err -> err.toString() to HttpServletResponse.SC_BAD_REQUEST },
                { result -> result to HttpServletResponse.SC_OK }
            )
}
