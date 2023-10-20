package com.abogomazov.merchant.guide.cli.parser

import com.abogomazov.merchant.guide.cli.parser.utils.CommandRegexBuilder
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CommandRegexBuilderTest : FreeSpec({

    "regex could be created using dsl" {
        val result = CommandRegexBuilder()
            .how().s().much().s().iz().s().RomanNum().s().question()
            .build()

        result.toString() shouldBe "how\\s+much\\s+is\\s+([MDCLXVI])\\s+\\?"
    }

})
