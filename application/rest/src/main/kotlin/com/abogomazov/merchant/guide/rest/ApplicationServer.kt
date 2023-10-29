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

// TODO WTF are these appBase, context and etc? RTFM!
// TODO Add Swagger
// TODO remove generating application.log by default
class ApplicationServer(
    private val setTranslationUseCase: SetTranslationUseCase,
    private val getTranslationUseCase: GetTranslationUseCase,
    private val setResourcePriceUseCase: SetResourceMarketPriceUseCase,
    private val getResourcePriceUseCase: GetResourceMarketPriceUseCase,
) : Application {

    private val server = Tomcat().apply {
        this.getConnector()
        this.getHost().appBase = "."
    }

    private val context = server
        .addContext("", File(".").absolutePath)

    override fun run() {
        server.registerEndpoints().start()
    }

    fun stop() {
        server.stop()
        server.destroy()
    }

    private fun Tomcat.registerEndpoints(): Tomcat {
        this.registerEndpoint(ROOT, UIServlet(indexPage()))
        this.registerEndpoint(ADD_TRANSLATION, SetTranslationServlet(setTranslationUseCase))
        this.registerEndpoint(GET_TRANSLATION, GetTranslationServlet(getTranslationUseCase))
        this.registerEndpoint(ADD_RESOURCE_PRICE, SetResourcePriceServlet(setResourcePriceUseCase))
        this.registerEndpoint(GET_RESOURCE_PRICE, GetResourcePriceServlet(getResourcePriceUseCase))

        return this
    }

    private fun Tomcat.registerEndpoint(route: String, servlet: HttpServlet) {
        this.registerServlet(servlet)
        context.registerRoute(route, servlet::class)
    }

    private fun Tomcat.registerServlet(servlet: HttpServlet) {
        this.addServlet(context.path, servlet::class.java.simpleName, servlet)
    }

    private fun Context.registerRoute(route: String, servlet: KClass<out HttpServlet>) {
        this.addServletMappingDecoded(route, servlet.java.simpleName)
    }
}

private fun indexPage() =
    BufferedReader(
        InputStreamReader(
            ApplicationServer::class.java.getResourceAsStream("/index.html")
                ?: error("Can't load index page")
        )
    ).readText()
