package com.abogomazov.merchant.guide.application

class ConsoleIO : InputReader, Printer {
    override fun read(): String {
        kotlin.io.print("> ")
        return readln()
    }

    override fun print(data: String) {
        println(data)
    }
}
