package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Resource

private fun errorMessage(reason: Any) = "[Error] $reason"

fun translationNotFound(localNum: LocalNumber) =
    errorMessage("Translation not found for \"$localNum\"")

fun localDigitAlreadyAssociated(localDigit: LocalDigit) =
    errorMessage("Digit \"$localDigit\" is already associated with roman digit")

fun romanNotationRulesViolated(localNum: LocalNumber) =
    errorMessage("Number \"$localNum\" violates roman notation rules")

fun resourcePriceNotFound(resource: Resource) =
    errorMessage("Price not found for \"$resource\" resource")

fun parsingFailed(reason: ParserError) =
    errorMessage("Command cannot be parsed: $reason")
