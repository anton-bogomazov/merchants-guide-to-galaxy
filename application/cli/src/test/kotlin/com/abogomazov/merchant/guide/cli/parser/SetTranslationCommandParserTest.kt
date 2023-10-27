package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.NullCommand
import com.abogomazov.merchant.guide.cli.NullParser
import com.abogomazov.merchant.guide.cli.commands.SetTranslationCommand
import com.abogomazov.merchant.guide.domain.galaxy.GalaxyNumeral
import com.abogomazov.merchant.guide.domain.galaxyNumeral
import com.abogomazov.merchant.guide.domain.roman.RomanNumeral
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData

class SetTranslationCommandParserTest : FreeSpec({

    "proper input successfully parsed" - {
        withData(
            case("glob is C", galaxyNumeral("glob"), RomanNumeral.C),
            case(" I is I", galaxyNumeral("I"), RomanNumeral.I),
            case("Credits is I", galaxyNumeral("Credits"), RomanNumeral.I),
        ) { (command, galaxyNumeral, romanDigit) ->
            SetTranslationCommandParser(NullParser).parse(command)
                .shouldBeRight(
                    SetTranslationCommand(galaxyNumeral, romanDigit)
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

private fun case(command: String, galaxyNumeral: GalaxyNumeral, romanNumeral: RomanNumeral) =
    Triple(command, galaxyNumeral, romanNumeral)
