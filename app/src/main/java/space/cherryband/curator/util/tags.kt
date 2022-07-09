package space.cherryband.curator.util

import space.cherryband.curator.data.Directory
import space.cherryband.curator.data.Image
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_EMPTY
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_HIDE


fun Map<String, Int>.get(dir: Directory): Int = getOrDefault(dir.path, TAG_EMPTY)
fun Map<Long, Int>.get(image: Image): Int = getOrDefault(image.id, TAG_EMPTY)

fun Map<String, Int>.getActiveTag(path: String): Int =
    get(path) ?: if (path.parents.isEmpty()) TAG_EMPTY else getActiveTag(path.parents)

fun Int?.isHidden() = this == TAG_HIDE