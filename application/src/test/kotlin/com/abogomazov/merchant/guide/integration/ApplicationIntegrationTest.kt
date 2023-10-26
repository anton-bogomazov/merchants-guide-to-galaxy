package com.abogomazov.merchant.guide.integration

import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.forAll

class ApplicationIntegrationTest : FreeSpec({

    "translator happy path scenario" {
        val commands = listOf(
            "glob is I" to "Set",
            // cannot associate the same local digit with 2 different roman numbers
            "glob is V" to "[Error] Digit \"glob\" is already associated with \"I\"",
            "prok is V" to "Set",
            "prok is V" to "Set",
            "pish is X" to "Set",
            "tegj is L" to "Set",
            "how much is pish tegj glob glob ?" to "pish tegj glob glob is 42",
            "boop is C" to "Set",
            "whoop is D" to "Set",
            // translation for groop is not found
            "how much is glob groop ?" to "[Error] Translation not found for \"groop\"",
            "groop is M" to "Set",
            // local number represents invalid roman number
            "how much is glob groop ?" to "[Error] Number \"glob groop\" violates roman notation rules",
            "how much is boop groop pish boop glob pish ?" to "boop groop pish boop glob pish is 999",
            // association could be overwritten with a new local digit
            "wtf is I" to "Set",
            "how much is wtf wtf wtf ?" to "wtf wtf wtf is 3",
            // ...old association has been deleted
            "how much is glob glob glob ?" to "[Error] Translation not found for \"glob\"",
            // re-set old association
            "glob is I" to "Set",
            "how much is glob glob glob ?" to "glob glob glob is 3",
        )

        runTest(commands.unzip().first, commands.unzip().second)
    }

    "market happy path scenario" {
        val commands = setDictionary() +
                listOf(
                    "glob glob Silver is 34 Credits" to "Set",
                    "glob prok Gold is 57800 Credits" to "Set",
                    "pish pish Iron is 3910 Credits" to "Set",
                    "how many Credits is glob prok Silver ?" to "glob prok Silver is 68 Credits",
                    "how many Credits is glob prok Gold ?" to "glob prok Gold is 57800 Credits",
                    "how many Credits is glob prok Iron ?" to "glob prok Iron is 782 Credits",
                    "glob glob Mud is 0 Credits" to "Set",
                    "how many Credits is groop groop groop Mud ?" to "groop groop groop Mud is 0 Credits",
                    "groop groop groop Mud is 1 Credits" to "Set",
                    "how many Credits is glob Mud ?" to "glob Mud is 0 Credits",
                    // Can't create resource 'Credits', the name is reserved
                    "pish pish Credits is 3910 Credits" to "[Error] Command cannot be parsed: InvalidArguments",
                    "how many Credits is glob prok Credits ?" to "[Error] Command cannot be parsed: InvalidArguments",
                )

        runTest(commands.unzip().first, commands.unzip().second)
    }

    "monkey test" {
        val commands = mutableListOf<String>()
        forAll<String> { commands.add(it) }
        runTest(commands, List(commands.size) { "I have no idea what you are talking about" })
    }
})
