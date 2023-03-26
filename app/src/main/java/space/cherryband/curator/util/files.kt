package space.cherryband.curator.util

import android.content.ContentUris
import android.provider.MediaStore

fun String.relativeFromStorage() = run {
        if (isAbsolute) {
            if (this == "/") return "Root"
            val paths = diced
            if (!paths[1].contains("storage"))
                throw IllegalArgumentException()

            if (paths[2].matches("[0-9A-F]{4}-[0-9A-F]{4}".toRegex()))
                paths.subList(2, paths.size - 2).joinToString(separator = "/")
            else
                paths.subList(3, paths.size - 2).joinToString(separator = "/")
        } else trimmed
    }

fun Long.toUri() =
        ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this
        )

private val String.trimmed
    get() = trim().trimEnd('/')


val String.isAbsolute get() = trim().startsWith('/')
val String.diced: List<String>
    get() = trimmed.split('/')
            .mapIndexed{ i, str -> if (i == 0 && str.isEmpty()) "/" else str}
            .filter(String::isNotEmpty)
val String.parents
    get() = trimmed.substringBeforeLast('/', "")
val String.leaf
    get() = if (this == "/") this else trimmed.substringAfterLast('/')
val String.depth
    get() = diced.size
val String.walk: List<String>
    get() = trimmed.diced.runningReduce { root, dir ->
        if (root.endsWith("/") || dir.endsWith("/")) "$root$dir".trimmed else "$root/$dir".trimmed
    }
fun String.isParentOf(other: String): Boolean = depth < other.depth && other.startsWith(this)