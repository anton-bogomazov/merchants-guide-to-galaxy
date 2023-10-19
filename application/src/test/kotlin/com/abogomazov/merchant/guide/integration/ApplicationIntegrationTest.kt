package com.abogomazov.merchant.guide.integration

import io.kotest.core.spec.style.FreeSpec

class ApplicationIntegrationTest : FreeSpec({

    "translator happy path scenario" {
        val commands = listOf(
            "glob is I" to "Set",
            // cannot associate the same local digit with 2 different roman numbers
            "glob is V" to "[Error] LocalDigitAlreadyAssociatedWithRoman",
            "prok is V" to "Set",
            "prok is V" to "Set",
            "pish is X" to "Set",
            "tegj is L" to "Set",
            "how much is pish tegj glob glob ?" to "pish tegj glob glob is 42",
            "boop is C" to "Set",
            "whoop is D" to "Set",
            // translation for groop is not found
            "how much is glob groop ?" to "[Error] TranslationNotFound",
            "groop is M" to "Set",
            // local number represents invalid roman number
            "how much is glob groop ?" to "[Error] NumberIsNotFollowingRomanNotationRules",
            "how much is boop groop pish boop glob pish ?" to "boop groop pish boop glob pish is 999",
            // association could be overwritten with a new local digit
            "wtf is I" to "Set",
            "how much is wtf wtf wtf ?" to "wtf wtf wtf is 3",
            // ...old association has been deleted
            "how much is glob glob glob ?" to "[Error] TranslationNotFound",
        )

        runTest(commands.unzip().first, commands.unzip().second)
    }

})
