package com.abogomazov.merchant.guide.rest.servlet

import arrow.core.flatMap
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumber
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class GetTranslationServlet(
    private val getTranslationUseCase: GetTranslationUseCase
) : HttpServlet() {

    companion object {
        private const val GALAXY_NUMBER = "galaxy"
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val galaxyNumber = parse(req)

        resp.writer.use { writer ->
            val (response, status) = execute(galaxyNumber)
            writer.write(response)
            resp.status = status
            writer.flush()
        }
    }

    private fun parse(req: HttpServletRequest) = req.getParameter(GALAXY_NUMBER)

    private fun execute(galaxyNumber: String): Pair<String, Int> =
        galaxyNumber.toGalaxyNumber().flatMap {
            getTranslationUseCase.execute(it)
                .map { result -> result.toInt().toString() }
        }.fold(
            { err -> err.toString() to HttpServletResponse.SC_BAD_REQUEST },
            { result -> result to HttpServletResponse.SC_OK }
        )
}
