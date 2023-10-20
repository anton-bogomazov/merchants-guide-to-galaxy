package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.roman.RomanDigit

private fun errorMessage(reason: Any) = "[Error] $reason"

fun translationNotFound(digit: LocalDigit) =
    errorMessage("Translation not found for \"$digit\"")

fun localDigitAlreadyAssociated(localDigit: LocalDigit, romanDigit: RomanDigit) =
    errorMessage("Digit \"$localDigit\" is already associated with \"$romanDigit\"")

fun romanNotationRulesViolated(localNum: LocalNumber) =
    errorMessage("Number \"$localNum\" violates roman notation rules")

fun resourcePriceNotFound(resource: Resource) =
    errorMessage("Price not found for \"$resource\" resource")

fun parsingFailed(reason: ParserError) =
    errorMessage("Command cannot be parsed: $reason")
