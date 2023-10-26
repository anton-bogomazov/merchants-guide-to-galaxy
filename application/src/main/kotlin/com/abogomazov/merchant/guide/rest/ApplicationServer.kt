package com.abogomazov.merchant.guide.rest

import com.abogomazov.merchant.guide.application.Application
import com.abogomazov.merchant.guide.usecase.market.GetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.market.SetResourceMarketPriceUseCase
import com.abogomazov.merchant.guide.usecase.translator.GetTranslationUseCase
import com.abogomazov.merchant.guide.usecase.translator.SetTranslationUseCase
import jakarta.servlet.http.HttpServlet
import org.apache.catalina.Context
import org.apache.catalina.startup.Tomcat
import java.io.File
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

        server.registerEndpoint(context, "/", UIServlet())
        server.registerEndpoint(context, "/add-translation", SetTranslationServlet(setTranslationUseCase))
        server.registerEndpoint(context, "/translation", GetTranslationServlet(getTranslationUseCase))
        server.registerEndpoint(context, "/add-resource-price", SetResourcePriceServlet(setResourcePriceUseCase))
        server.registerEndpoint(context, "/resource-price", GetResourcePriceServlet(getResourcePriceUseCase))

        server.start()
        server.getServer().await()
    }
}

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
