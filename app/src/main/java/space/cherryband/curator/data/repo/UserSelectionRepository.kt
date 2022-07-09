package space.cherryband.curator.data.repo

import space.cherryband.curator.data.Directory
import space.cherryband.curator.data.Image
import space.cherryband.curator.util.parents

object UserSelectionRepository {
    private val selectedDirs = HashMap<String, Int>()
    private val selectedPhotos = HashMap<Long, Int>()

    const val TAG_EMPTY = Int.MAX_VALUE
    const val TAG_HIDE = Int.MIN_VALUE

    fun isSelected(dir: Directory) = selectedDirs.containsKey(dir.path)
    fun isSelected(image: Image) = selectedPhotos.containsKey(image.id)

    var Directory.tag
        set(tag) = setTag(path, tag)
        get() = selectedDirs[path] ?: TAG_EMPTY
    var Image.tag
        set(tag) = setTag(id, tag)
        get() = selectedPhotos[id] ?: TAG_EMPTY

    fun setTag(path: String, tag: Int?) {
        if (tag != null && tag != TAG_EMPTY)
            selectedDirs[path] = tag
        else selectedDirs.remove(path)
    }
    fun setTag(id: Long, tag: Int?) {
        if (tag != null && tag != TAG_EMPTY)
            selectedPhotos[id] = tag
        else selectedPhotos.remove(id)
    }

    fun getTag(path: String) = selectedDirs[path] ?: TAG_EMPTY
    fun getTag(id: Long) = selectedPhotos[id] ?: TAG_EMPTY

    fun dirTags(): Map<String, Int> = selectedDirs.toMap()
    fun imageTags(): Map<Long, Int> = selectedPhotos.toMap()
}