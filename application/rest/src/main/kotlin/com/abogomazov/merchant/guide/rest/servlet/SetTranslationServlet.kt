package com.abogomazov.merchant.guide.rest.servlet

import arrow.core.flatMap
import arrow.core.raise.either
import com.abogomazov.merchant.guide.domain.galaxy.toGalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.toRomanNumeral
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class SetTranslationServlet(
    private val setTranslationUseCase: SetTranslationUseCase
) : HttpServlet() {
    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val dto = parse(req)

        resp.writer.use { writer ->
            val (response, status) = execute(dto)
            writer.write(response)
            resp.status = status
            writer.flush()
        }
    }

    private fun parse(req: HttpServletRequest) =
        req.reader.use {
            Json.decodeFromString<SetTranslationDto>(it.readText())
        }

    private fun execute(dto: SetTranslationDto): Pair<String, Int> =
        either { dto.galaxy.toGalaxyNumeral().bind() to dto.roman.toRomanNumeral() }
            .flatMap { (localDigit, romanDigit) ->
                setTranslationUseCase.execute(localDigit, romanDigit)
                    .map { "OK" }
            }.fold(
                { err -> err.toString() to HttpServletResponse.SC_BAD_REQUEST },
                { result -> result to HttpServletResponse.SC_OK }
            )
}

@Serializable
private data class SetTranslationDto(
    val galaxy: String,
    val roman: String,
)
