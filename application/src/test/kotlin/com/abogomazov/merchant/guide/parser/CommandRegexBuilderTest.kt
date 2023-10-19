package com.abogomazov.merchant.guide.parser

import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class CommandRegexBuilderTest : FreeSpec({

    "regex could be created using dsl" {
        val result = CommandRegexBuilder()
            .how().s().much().s().iz().s().romanNum().s().question()
            .build()

        result.toString() shouldBe "how\\s+much\\s+is\\s+([MDCLXVI])\\s+\\?"
    }

    "local number capturing group captures whole number" - {
        withData(
            "One",
            "One two",
            "One two three oclock",
            "One two three oclock four oclock rock"
        ) {
            val regex = CommandRegexBuilder().localNum().build()

            regex.find(it)?.value shouldBe it
        }
    }

})
