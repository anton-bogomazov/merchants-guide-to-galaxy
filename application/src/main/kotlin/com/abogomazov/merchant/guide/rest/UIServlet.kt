package com.abogomazov.merchant.guide.rest

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class UIServlet : HttpServlet() {

    private val index = indexPage()

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.writer?.use {
            it.write(index)
            it.flush()
        }
    }
}

private fun indexPage() = BufferedReader(InputStreamReader(
    UIServlet::class.java.getResourceAsStream("/index.html"))).readText()
