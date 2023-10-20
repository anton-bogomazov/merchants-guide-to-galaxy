package com.abogomazov.merchant.guide.integration

import io.kotest.core.spec.style.FreeSpec

class ApplicationLoadTest : FreeSpec({

    "overload by calculations".config(enabled = false) {
        val commands = setDictionary().unzip().first +
                List(1_000_000) { "how much is boop groop pish boop glob pish" }

        runTest(commands, emptyList())
    }
})
