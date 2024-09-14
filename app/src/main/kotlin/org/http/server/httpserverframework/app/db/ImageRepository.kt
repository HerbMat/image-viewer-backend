package org.http.server.httpserverframework.app.db

import org.http.server.httpserverframework.app.domain.Image
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import kotlin.io.path.extension
import kotlin.io.path.isDirectory

class ImageRepository {
    private val imagesPaths =  run {
        Files.walk(Path.of("/home/matik/WebstormProjects/navbar/dist/img"))
            .parallel()
            .filter { !it.isDirectory() }
            .filter { it.extension == "jpg" }
            .map { Image(it, it.fileName.toString().removePrefix("frame").removeSuffix(".jpg").toInt()) }
            .sorted{ image1, image2 -> image1.pos.compareTo(image2.pos)}
            .collect(Collectors.toList())
            .toList()
    }

    fun count(): Int {
        return imagesPaths.size
    }

    fun find(pos: Int): Path? {
        if (pos < 0 || pos >= imagesPaths.size) {
            return null
        }
        return imagesPaths[pos].path
    }

    fun findRange(start: Int, end: Int = 1): List<Path> {
        if (start < 0 || end < 0 || start > end) {
            return listOf()
        }
        val listSize = imagesPaths.size
        if (start >= listSize) {
            return listOf()
        }
        return imagesPaths
            .subList(start, if (end >= listSize) listSize -1 else end )
            .map { it.path }
    }
}