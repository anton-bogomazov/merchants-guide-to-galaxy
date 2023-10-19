package com.abogomazov.merchant.guide.application

class ConsoleIO : InputReader, Printer {
    override fun read(): String {
        return readln()
    }

    override fun print(data: String) {
        println(data)
    }
}
