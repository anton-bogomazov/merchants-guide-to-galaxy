package com.abogomazov.merchant.guide.rest

import com.abogomazov.merchant.guide.cli.parser.toGalaxyNumber
import com.abogomazov.merchant.guide.cli.parser.toResource
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class GetResourcePriceServlet(
    private val getResourcePriceUseCase: GetResourceMarketPriceUseCase
) : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val resourceName = req?.getParameter("resourceName") ?: return
        val localNumber = req?.getParameter("resourceAmount") ?: return

        resp?.writer?.use { writer ->
            resourceName.toResource().map {
                getResourcePriceUseCase.execute(
                    localNumber.toGalaxyNumber(),
                    it
                ).map { result ->
                    writer.write(result.toString())
                    resp.status = HttpServletResponse.SC_OK
                }.mapLeft { error ->
                    writer.write(error.toString())
                    resp.status = HttpServletResponse.SC_BAD_REQUEST
                }
                writer.flush()
            }
        } ?: resp?.apply {
            status = HttpServletResponse.SC_BAD_REQUEST
        }
    }
}
