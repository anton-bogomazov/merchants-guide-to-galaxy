package com.abogomazov.merchant.guide.rest.servlet

import arrow.core.flatMap
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumber
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GetTranslationServlet(
    private val getTranslationUseCase: GetTranslationUseCase
) : HttpServlet() {

    companion object {
        private const val GALAXY_NUMBER = "galaxy"
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val galaxyNumber = parse(req)

        resp.writer.use { writer ->
            val response = execute(galaxyNumber)
            writer.write(Json.encodeToString(response))
            resp.status = response.code
            writer.flush()
        }
    }

    private fun parse(req: HttpServletRequest) = req.getParameter(GALAXY_NUMBER)

    private fun execute(galaxyNumber: String): Response =
        galaxyNumber.toGalaxyNumber().flatMap {
            getTranslationUseCase.execute(it)
                .map { result -> result.toInt().toString() }
        }.toResponse()
}
