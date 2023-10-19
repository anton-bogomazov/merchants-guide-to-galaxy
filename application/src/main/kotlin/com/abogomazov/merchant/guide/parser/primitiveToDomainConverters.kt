package com.abogomazov.merchant.guide.parser

import arrow.core.Either
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.roman.RomanDigit

fun String.toLocalNumber() = LocalNumber(
    this.sanitized().split(" ").map { LocalDigit(it) }
)

fun String.toLocalDigit() = LocalDigit(this)

fun String.toRomanDigit() = RomanDigit.valueOf(this)

fun String.toResource(): Either<ParserError.InvalidArguments, Resource> =
    Resource.from(this)
        .mapLeft { ParserError.InvalidArguments }


fun String.sanitized() = this.replace("\\s+".toRegex(), " ").trim()
