package space.cherryband.curator.util

import android.content.ContentUris
import android.provider.MediaStore

fun getRelativeDirFromStorage(path: String): String {
    return if (path.startsWith('/')){
        val paths = path.split('/')
        if (!paths[0].contains("storage"))
            throw IllegalArgumentException()

        if (paths[1].matches("[0-9A-F]{4}-[0-9A-F]{4}".toRegex()))
            paths.subList(2, paths.size - 2).joinToString(separator = "/")
        else
            paths.subList(3, paths.size - 2).joinToString(separator = "/")
    } else path
}

fun imageIdToUri(id: Long) =
    ContentUris.withAppendedId(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
    )