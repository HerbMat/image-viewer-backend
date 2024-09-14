package org.http.server.httpserverframework.app.service

import org.http.server.httpserverframework.app.db.ImageRepository
import org.http.server.httpserverframework.app.domain.Image
import java.io.File
import java.nio.channels.SeekableByteChannel
import java.nio.file.Files

class ImageService(private val imageRepository: ImageRepository) {
    fun getImage(pos: Int): File? {
        return imageRepository.find(pos)?.toFile()
    }
}

/**
 * package org.http.server.httpserverframework.app.service
 *
 * import org.http.server.httpserverframework.app.db.ImageRepository
 * import org.http.server.httpserverframework.app.domain.Image
 * import java.nio.file.Files
 *
 * class ImageService(private val imageRepository: ImageRepository) {
 *     fun getImage(pos: Int): ByteArray {
 *         val imagePath = imageRepository.find(pos)
 *         return Files.readAllBytes(imagePath)
 *     }
 * }
 */