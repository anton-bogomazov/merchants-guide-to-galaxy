package com.abogomazov.merchant.guide.application.inmemory

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class KeyValueInMemoryStorageTest : FreeSpec({

    "should store and retrieve values correctly" {
        val sut = KeyValueInMemoryStorage<String, Int>()
        sut.set("one", 1)
        
        sut.get("one") shouldBe 1
    }

    "should retrieve keys by values" {
        val sut = KeyValueInMemoryStorage<String, Int>()
        sut.set("one", 1)
        
        sut.getByValue(1) shouldBe "one"
    }

    "should delete keys correctly" {
        val sut = KeyValueInMemoryStorage<String, Int>()
        sut.set("three", 3)

        sut.delete("three")

        sut.get("three") shouldBe null
    }

    "should handle non-existing keys" {
        val sut = KeyValueInMemoryStorage<String, Int>()
        sut.delete("non-existing") shouldBe null
    }
})
