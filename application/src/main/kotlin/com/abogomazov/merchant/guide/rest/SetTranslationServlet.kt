package com.abogomazov.merchant.guide.rest

import com.abogomazov.merchant.guide.cli.parser.toGalaxyNumeral
import com.abogomazov.merchant.guide.cli.parser.toRomanNumeral
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class SetTranslationServlet(
    private val setTranslationUseCase: SetTranslationUseCase
) : HttpServlet() {
    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val dto = req?.reader?.use {
            Json.decodeFromString<SetTranslationDto>(it.readText())
        }

        dto?.let {
            setTranslationUseCase.execute(
                dto.localDigit.toGalaxyNumeral(),
                dto.romanDigit.toRomanNumeral()
            )
        } ?: resp?.apply {
            status = HttpServletResponse.SC_BAD_REQUEST
        }
    }
}

@Serializable
private data class SetTranslationDto(
    val localDigit: String,
    val romanDigit: String,
)
