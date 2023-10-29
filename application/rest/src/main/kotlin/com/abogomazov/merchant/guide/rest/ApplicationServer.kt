package com.abogomazov.merchant.guide.rest

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.rest.servlet.GetResourcePriceServlet
import com.abogomazov.merchant.guide.rest.servlet.GetTranslationServlet
import com.abogomazov.merchant.guide.rest.servlet.SetResourcePriceServlet
import com.abogomazov.merchant.guide.rest.servlet.SetTranslationServlet
import com.abogomazov.merchant.guide.rest.servlet.UIServlet
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import jakarta.servlet.http.HttpServlet
import org.apache.catalina.Context
import org.apache.catalina.startup.Tomcat
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.reflect.KClass

class ApplicationServer(
    private val setTranslationUseCase: SetTranslationUseCase,
    private val getTranslationUseCase: GetTranslationUseCase,
    private val setResourcePriceUseCase: SetResourceMarketPriceUseCase,
    private val getResourcePriceUseCase: GetResourceMarketPriceUseCase,
) : Application {
    override fun run() {
        val server = Tomcat().apply {
            this.getConnector()
            this.getHost().appBase = "."
        }
        val context = server.addContext("", File(".").absolutePath)

        server.registerEndpoint(context, ROOT, UIServlet(indexPage()))
        server.registerEndpoint(context, ADD_TRANSLATION, SetTranslationServlet(setTranslationUseCase))
        server.registerEndpoint(context, GET_TRANSLATION, GetTranslationServlet(getTranslationUseCase))
        server.registerEndpoint(context, ADD_RESOURCE_PRICE, SetResourcePriceServlet(setResourcePriceUseCase))
        server.registerEndpoint(context, GET_RESOURCE_PRICE, GetResourcePriceServlet(getResourcePriceUseCase))

        server.start()
        server.getServer().await()
    }
}

private fun indexPage() =
    BufferedReader(
        InputStreamReader(
            ApplicationServer::class.java.getResourceAsStream("/index.html")
                ?: error("Can't load index page")
        )
    ).readText()

fun Tomcat.registerEndpoint(context: Context, route: String, servlet: HttpServlet) {
    this.registerServlet(context, servlet)
    context.registerRoute(route, servlet::class)
}

fun Tomcat.registerServlet(context: Context, servlet: HttpServlet) {
    this.addServlet(context.path, servlet::class.java.simpleName, servlet)
}

fun Context.registerRoute(route: String, servlet: KClass<out HttpServlet>) {
    this.addServletMappingDecoded(route, servlet.java.simpleName)
}
