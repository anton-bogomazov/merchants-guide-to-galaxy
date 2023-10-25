package com.abogomazov.merchant.guide.cli.parser

import arrow.core.left
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Credits
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import java.math.BigInteger

fun String.toLocalNumber() = LocalNumber(
    this.sanitized().split(" ").map { LocalDigit(it) }
)

fun String.toLocalDigit() = LocalDigit(this)

fun String.toRomanDigit() = RomanDigit.valueOf(this)

fun String.toResource() = Resource.from(this)
        .mapLeft { ParserError.InvalidArguments }

fun String.toCredit() =
    try {
        Credits.from(BigInteger(this@toCredit))
            .mapLeft { ParserError.InvalidArguments }
    } catch (e: NumberFormatException) {
        ParserError.InvalidArguments.left()
    }

private fun String.sanitized() = this.replace("\\s+".toRegex(), " ").trim()
