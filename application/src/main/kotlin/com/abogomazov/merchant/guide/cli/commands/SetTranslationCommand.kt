package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationError
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase

data class SetTranslationCommand(
    private val localDigit: LocalDigit,
    private val romanDigit: RomanDigit,
) : BusinessCommand {

    fun execute(usecase: SetTranslationUseCase) =
        usecase.execute(localDigit, romanDigit)
            .fold({ it.toError() }, { response() })

    private fun response() = "Set"

    private fun SetTranslationError.toError() =
        when (this) {
            is SetTranslationError.LocalDigitAlreadyAssociated ->
                localDigitAlreadyAssociated(localDigit, this.romanDigit)
        }
}
