package com.abogomazov.merchant.guide.cli.commands

import com.abogomazov.merchant.guide.cli.parser.ParserError
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumber
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.roman.RomanDigit

private fun errorMessage(reason: Any) = "[Error] $reason"

fun translationNotFound(digit: GalaxyNumeral) =
    errorMessage("Translation not found for \"$digit\"")

fun galaxyNumeralAlreadyAssociated(galaxyNumeral: GalaxyNumeral, romanDigit: RomanDigit) =
    errorMessage("Digit \"$galaxyNumeral\" is already associated with \"$romanDigit\"")

fun romanNotationRulesViolated(galaxyNumber: GalaxyNumber) =
    errorMessage("Number \"$galaxyNumber\" violates roman notation rules")

fun resourcePriceNotFound(resource: Resource) =
    errorMessage("Price not found for \"$resource\" resource")

fun parsingFailed(reason: ParserError) =
    errorMessage("Command cannot be parsed: $reason")
