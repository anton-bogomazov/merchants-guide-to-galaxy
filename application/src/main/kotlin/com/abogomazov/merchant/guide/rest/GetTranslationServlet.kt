package com.abogomazov.merchant.guide.rest

import com.abogomazov.merchant.guide.cli.parser.toGalaxyNumber
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class GetTranslationServlet(
    private val getTranslationUseCase: GetTranslationUseCase
) : HttpServlet() {
    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req?.getParameter("localNumber")
        val localNumber = req?.getParameter("localNumber")

        localNumber?.let {
            resp?.writer.use {
                getTranslationUseCase.execute(
                    localNumber.toGalaxyNumber()
                ).map { result ->
                    it?.write(result.toString())
                    resp?.status = HttpServletResponse.SC_OK
                }.mapLeft { error ->
                    it?.write(error.toString())
                    resp?.status = HttpServletResponse.SC_BAD_REQUEST
                }
                it?.flush()
            }
        } ?: resp?.apply {
            status = HttpServletResponse.SC_BAD_REQUEST
        }
    }
}
