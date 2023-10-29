package com.abogomazov.merchant.guide.cli

object ConsoleIO : CommandSource, ResultCollector {
    override fun read(): String {
        print("> ")
        return readln()
    }

    override fun push(data: String) {
        println(data)
    }
}
