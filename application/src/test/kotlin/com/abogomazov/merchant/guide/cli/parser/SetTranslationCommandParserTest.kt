package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullCommand
import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.cli.commands.SetTranslationCommand
import com.abogomazov.merchant.guide.domain.dirt
import com.abogomazov.merchant.guide.domain.local.LocalDigit
import com.abogomazov.merchant.guide.domain.local.LocalNumber
import com.abogomazov.merchant.guide.domain.localDigit
import com.abogomazov.merchant.guide.domain.localNumber
import com.abogomazov.merchant.guide.domain.market.Resource
import com.abogomazov.merchant.guide.domain.roman.RomanDigit
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class SetTranslationCommandParserTest : FreeSpec({

    "proper input successfully parsed" - {
        withData(
            case("glob is C", localDigit("glob"), RomanDigit.C),
            case(" I is I", localDigit("I"), RomanDigit.I),
            case("Credits is I", localDigit("Credits"), RomanDigit.I),
        ) { (command, localDigit, romanDigit) ->
            SetTranslationCommandParser(NullParser).parse(command)
                .shouldBeRight(
                    SetTranslationCommand(localDigit, romanDigit)
                )
        }
    }

    "malformed input is not parsed as SetTranslationCommandParserTest" - {
        withData(
            "glob is VV",
            "glob is IV",
            "glob is V V",
            "glob is is V V",
        ) {
            SetTranslationCommandParser(NullParser).parse(it)
                .shouldBeRight(NullCommand)
        }
    }
})

private fun case(command: String, localDigit: LocalDigit, romanDigit: RomanDigit) =
    Triple(command, localDigit, romanDigit)
