package com.abogomazov.merchant.guide.cli.parser

sealed interface ParserError {
    data object FailedToExtractArguments : ParserError
    data object InvalidArguments : ParserError
}
