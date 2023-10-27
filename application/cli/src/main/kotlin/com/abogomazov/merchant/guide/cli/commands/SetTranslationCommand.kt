package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase

data class SetTranslationCommand(
    private val galaxyNumeral: GalaxyNumeral,
    private val romanNumeral: RomanNumeral,
) : BusinessCommand {

    fun execute(usecase: SetTranslationUseCase) =
        usecase.execute(galaxyNumeral, romanNumeral)
            .fold({ it.toError() }, { "Set" })

    private fun SetTranslationError.toError() =
        when (this) {
            is SetTranslationError.GalaxyNumeralAlreadyAssociated ->
                galaxyNumeralAlreadyAssociated(galaxyNumeral, this.romanNumeral)
        }
}
