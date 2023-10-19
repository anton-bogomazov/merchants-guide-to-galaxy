package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCaseError

data class GetTranslationCommand(
    private val localNum: LocalNumber,
) : Command {

    fun execute(usecase: GetTranslationUseCase) =
        usecase.execute(localNum)
            .fold({ it.toError() }, { response(it) })

    private fun response(result: Amount) = "$localNum is ${result.toInt()}"
}

private fun GetTranslationUseCaseError.toError() =
    when (this) {
        GetTranslationUseCaseError.TranslationNotFound -> errorMessage(this)
        GetTranslationUseCaseError.NumberIsNotFollowingRomanNotationRules -> errorMessage(this)
    }
