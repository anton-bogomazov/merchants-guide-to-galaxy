package com.abogomazov.merchant.guide.parser

sealed interface ParserError {
    data object FailedToExtractArguments : ParserError
    data object InvalidArguments : ParserError
}
