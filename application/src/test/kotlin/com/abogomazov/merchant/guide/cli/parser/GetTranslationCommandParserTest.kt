package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.GetTranslationCommand
import com.abogomazov.merchant.guide.domain.galaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxyNumber
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class GetTranslationCommandParserTest : FreeSpec({

    "proper input successfully parsed" - {
        withData(
            "   how much is\t glob   prok \nglob  ok   ? " to
                    galaxyNumber(galaxyNumeral("glob"), galaxyNumeral("prok"), galaxyNumeral("glob"), galaxyNumeral("ok")),
           "how much is how is ?" to galaxyNumber(galaxyNumeral("how"), galaxyNumeral("is")),
            "how much is much ?" to galaxyNumber(galaxyNumeral("much")),

        ) { (command, number) ->
            GetTranslationCommandParser(NullParser).parse(command)
                .shouldBeRight(
                    GetTranslationCommand(
                        galaxyNumber = number
                    )
                )
        }
    }
})
