package com.abogomazov.merchant.guide.application

class ConsoleIO : CommandSource, ResultCollector {
    override fun read(): String {
        kotlin.io.print("> ")
        return readln()
    }

    override fun push(data: String) {
        println(data)
    }
}
