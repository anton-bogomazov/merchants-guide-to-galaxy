package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCaseError

data class SetTranslationCommand(
    private val localDigit: LocalDigit,
    private val romanDigit: RomanDigit,
) : Command {

    fun execute(usecase: SetTranslationUseCase) =
        usecase.execute(localDigit, romanDigit)
            .fold({ it.toError() }, { response() })

    private fun response() = ""

}

private fun SetTranslationUseCaseError.toError() =
    when (this) {
        SetTranslationUseCaseError.LocalDigitAlreadyAssociatedWithRoman -> errorMessage(this)
    }
