package com.abogomazov.merchant.guide.rest.servlet

import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class UIServlet(
    private val page: String
) : HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.writer.use {
            it.write(page)
            it.flush()
        }
    }
}
