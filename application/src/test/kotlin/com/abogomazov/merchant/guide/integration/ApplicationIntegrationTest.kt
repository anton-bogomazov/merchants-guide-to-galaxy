package com.abogomazov.merchant.guide.integration

import io.kotest.core.spec.style.FreeSpec

class ApplicationIntegrationTest : FreeSpec({

    "happy path scenario" {
        val commands = listOf(
            "glob is I" to "Set",
            "prok is V" to "Set",
            "pish is X" to "Set",
            "tegj is L" to "Set",
            "how much is pish tegj glob glob ?" to "pish tegj glob glob is 42",
            "boop is C" to "Set",
            "whoop is D" to "Set",
            "how much is glob groop ?" to "[Error] TranslationNotFound",
            "groop is M" to "Set",
            "how much is glob groop ?" to "[Error] NumberIsNotFollowingRomanNotationRules",
            "how much is boop groop pish boop glob pish ?" to "boop groop pish boop glob pish is 999"
        )

        runTest(commands.unzip().first, commands.unzip().second)
    }

})
