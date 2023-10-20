package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.roman.Amount
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationError

data class GetTranslationCommand(
    private val localNum: LocalNumber,
) : BusinessCommand {

    fun execute(usecase: GetTranslationUseCase) =
        usecase.execute(localNum)
            .fold({ it.toError() }, { response(it) })

    private fun response(result: Amount) = "$localNum is ${result.toInt()}"
}

private fun GetTranslationError.toError() =
    when (this) {
        GetTranslationError.TranslationNotFound -> errorMessage(this)
        GetTranslationError.RomanNotationRulesViolated -> errorMessage(this)
    }
