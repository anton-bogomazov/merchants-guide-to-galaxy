package com.abogomazov.merchant.guide.rest

import com.abogomazov.merchant.guide.rest.servlet.Response
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ApplicationServerTest : FreeSpec({

    "servlet test" {
        val server = resultAppServer()
        server.run()

        val client = MerchantRestClient()

        client.getTranslation(listOf("glob", "prok", "rock")) shouldBe okResponse("10")
        client.setTranslation("glob", "I") shouldBe okResponse("OK")
        client.setResourcePrice(listOf("glob", "prok", "rock"), "dirt", "42") shouldBe okResponse("OK")
        client.getResourcePrice(listOf("glob", "prok", "rock"), "dirt") shouldBe okResponse("34")

        server.stop()
    }

    "servlet test fail" {
        val server = errorAppServer()
        server.run()

        val client = MerchantRestClient()

        client.getTranslation(listOf("glob", "prok", "rock")) shouldBe
                errResponse("RomanNotationRulesViolated")
        client.setTranslation("glob", "I") shouldBe
                errResponse("GalaxyNumeralAlreadyAssociated(romanNumeral=D)")
        client.setResourcePrice(listOf("glob", "prok", "rock"), "dirt", "42") shouldBe
                errResponse("RomanNotationRulesViolated")
        client.getResourcePrice(listOf("glob", "prok", "rock"), "dirt") shouldBe
                errResponse("PriceNotFound")

        server.stop()
    }
})

fun okResponse(result: String) = Response(result, 200)
fun errResponse(result: String) = Response(result, 400)
