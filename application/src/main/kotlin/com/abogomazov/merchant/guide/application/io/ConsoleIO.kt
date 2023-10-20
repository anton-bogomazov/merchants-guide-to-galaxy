package com.abogomazov.merchant.guide.application.io

import com.abogomazov.merchant.guide.cli.CommandSource
import com.abogomazov.merchant.guide.cli.ResultCollector

class ConsoleIO : CommandSource, ResultCollector {
    override fun read(): String {
        print("> ")
        return readln()
    }

    override fun push(data: String) {
        println(data)
    }
}
