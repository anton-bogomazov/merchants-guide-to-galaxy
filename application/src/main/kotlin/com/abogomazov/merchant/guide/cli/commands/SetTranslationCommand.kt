package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase

data class SetTranslationCommand(
    private val galaxyNumeral: GalaxyNumeral,
    private val romanDigit: RomanDigit,
) : BusinessCommand {

    fun execute(usecase: SetTranslationUseCase) =
        usecase.execute(galaxyNumeral, romanDigit)
            .fold({ it.toError() }, { "Set" })

    private fun SetTranslationError.toError() =
        when (this) {
            is SetTranslationError.GalaxyNumeralAlreadyAssociated ->
                galaxyNumeralAlreadyAssociated(galaxyNumeral, this.romanDigit)
        }
}
