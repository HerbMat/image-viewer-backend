package org.http.server.httpserverframework.app.api.method

import com.sun.net.httpserver.HttpExchange
import org.http.server.httpserverframework.server.bind.annotation.Controller
import org.http.server.httpserverframework.app.service.ImageService
import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
import org.http.server.httpserverframework.server.http.HttpCodes
import org.http.server.httpserverframework.server.http.HttpMethod
import org.http.server.httpserverframework.log.Logger
import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addCORSHeaders
import org.http.server.httpserverframework.server.test.utils.PathVariableMethods
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption

@Controller(path = "/images/{id}")
class GetImageHttpMethod(
    private val imageService: ImageService
): RequestHandlerMethod() {
    override val path = "/images/{$PATH_ID}"
    override val method = HttpMethod.GET

    companion object {
        private val log = Logger.getLogger(this::class.toString())
        private const val PATH_ID = "id"
    }

    override fun handle(httpExchange: HttpExchange) {
        val pathVariables = PathVariableMethods.extractPathVariables(httpExchange.requestURI.toString(), path)
        pathVariables[PATH_ID]?.also {
            val imageFile = imageService.getImage(it.toInt())!!
            FileChannel.open(imageFile.toPath(), StandardOpenOption.READ).use {
                imageChannel ->
                httpExchange.addCORSHeaders()
                httpExchange.responseHeaders.set("Content-Type", "image/jpeg")
                httpExchange.sendResponseHeaders(HttpCodes.OK, imageFile.length())
                imageChannel.transferTo(0, imageFile.length(), Channels.newChannel(httpExchange.responseBody))
            }
        } ?: run {
            httpExchange.addCORSHeaders()
            httpExchange.sendResponseHeaders(HttpCodes.BAD_REQUEST, 0)
        }
        httpExchange.close()
    }
}

/***
 * package org.http.server.httpserverframework.app.api.method
 *
 * import com.sun.net.httpserver.HttpExchange
 * import org.http.server.httpserverframework.server.bind.annotation.Controller
 * import org.http.server.httpserverframework.app.db.ImageRepository
 * import org.http.server.httpserverframework.app.service.ImageService
 * import org.http.server.httpserverframework.server.handlers.methods.RequestHandlerMethod
 * import org.http.server.httpserverframework.server.http.HttpCodes
 * import org.http.server.httpserverframework.server.http.HttpMethod
 * import org.http.server.httpserverframework.log.Logger
 * import org.http.server.httpserverframework.server.test.utils.HttpExchangeExtensions.addCORSHeaders
 * import org.http.server.httpserverframework.server.test.utils.PathVariableMethods
 *
 * @Controller(path = "/image/{id}")
 * class GetImageHttpMethod(
 *     private val imageService: ImageService
 * ): RequestHandlerMethod() {
 *     override val path = "/images/{$PATH_ID}"
 *     override val method = HttpMethod.GET
 *
 *     companion object {
 *         private val log = Logger.getLogger(this::class.toString())
 *         private const val PATH_ID = "id"
 *     }
 *
 *     override fun handle(httpExchange: HttpExchange) {
 *         val pathVariables = PathVariableMethods.extractPathVariables(httpExchange.requestURI.toString(), path)
 *         pathVariables[PATH_ID]?.also {
 *             val imageContent = imageService.getImage(it.toInt())
 *             httpExchange.responseBody.use {
 *                     responseBodyStream ->
 *                 httpExchange.addCORSHeaders()
 *                 httpExchange.responseHeaders.set("Content-Type", "image/jpeg")
 *                 httpExchange.sendResponseHeaders(HttpCodes.OK, imageContent.size.toLong())
 *                 responseBodyStream.write(imageContent)
 *             }
 *         } ?: run {
 *             httpExchange.addCORSHeaders()
 *             httpExchange.sendResponseHeaders(HttpCodes.BAD_REQUEST, 0)
 *         }
 *         httpExchange.close()
 *     }
 * }
 */