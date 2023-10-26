package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.domain.localDigit
import com.abogomazov.merchant.guide.domain.localNumber
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class GetTranslationCommandParserTest : FreeSpec({

    "proper input successfully parsed" - {
        withData(
            "   how much is\t glob   prok \nglob  ok   ? " to
                    localNumber(localDigit("glob"), localDigit("prok"), localDigit("glob"), localDigit("ok")),
           "how much is how is ?" to localNumber(localDigit("how"), localDigit("is")),
            "how much is much ?" to localNumber(localDigit("much")),

        ) { (command, number) ->
            GetTranslationCommandParser(NullParser).parse(command)
                .shouldBeRight(
                    GetTranslationCommand(
                        localNum = number
                    )
                )
        }
    }
})
