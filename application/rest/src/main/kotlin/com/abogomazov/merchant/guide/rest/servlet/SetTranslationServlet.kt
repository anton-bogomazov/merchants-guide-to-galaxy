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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SetTranslationServlet(
    private val setTranslationUseCase: SetTranslationUseCase
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
            Json.decodeFromString<SetTranslationDto>(it.readText())
        }

    private fun execute(dto: SetTranslationDto): Response =
        either { dto.galaxy.toGalaxyNumeral().bind() to dto.roman.toRomanNumeral().bind() }
            .flatMap { (localDigit, romanDigit) ->
                setTranslationUseCase.execute(romanDigit, localDigit)
                    .map { "OK" }
            }.toResponse()
}

@Serializable
data class SetTranslationDto(
    val galaxy: String,
    val roman: String,
)
