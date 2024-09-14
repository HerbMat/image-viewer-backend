package org.http.server.httpserverframework.app.api.method

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.bind.annotation.Controller
import org.http.server.httpserverframework.app.db.ImageRepository
import org.http.server.httpserverframework.app.service.ImageService
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpCodes
import org.http.server.httpserverframework.server.http.HttpMethod
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addCORSHeaders
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods

@Controller(path = "/images/count")
class GetImageCountHttpMethod(
    private val imageRepository: ImageRepository
): RequestHandlerMethod() {
    override val path = "/images/count"
    override val method = HttpMethod.GET

    companion object {
        private val log = Logger.getLogger(this::class.toString())
    }

    override fun handle(httpExchange: HttpExchange) {
        val responseBody = "{ \"count\": ${imageRepository.count()} }"
        httpExchange.responseBody.use {
                responseBodyStream ->
            httpExchange.addCORSHeaders()
            httpExchange.responseHeaders.set("Content-Type", "application/json")
            httpExchange.sendResponseHeaders(HttpCodes.OK, responseBody.toByteArray().size.toLong())
            responseBodyStream.write(responseBody.toByteArray())
        }
        httpExchange.close()
    }
}