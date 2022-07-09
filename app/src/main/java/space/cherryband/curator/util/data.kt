package space.cherryband.curator.util

import space.cherryband.curator.data.Directory
import space.cherryband.curator.data.Path
import space.cherryband.curator.ui.viewmodel.DirectoryViewModel
import kotlin.math.min

fun Comparator<String>.recursively() = Comparator<Path> { path1, path2 ->
    val diced1 = path1.path.diced
    val diced2 = path2.path.diced
    val depth1 = diced1.size
    val depth2 = diced2.size

    for (index in 0 until min(depth1, depth2)) { // compare the names of directories at the same level going down from root
        val comp = compare(diced1[index], diced2[index])
        if (comp != 0) return@Comparator comp // return immediately when there's a difference
    }

    return@Comparator depth1 - depth2 // one is a subdirectory of the other; parent is smaller
}

val recursiveNaturalPath = String.Companion.CASE_INSENSITIVE_ORDER.recursively()