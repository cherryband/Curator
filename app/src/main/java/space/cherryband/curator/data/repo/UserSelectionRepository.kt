package space.cherryband.curator.data.repo

import space.cherryband.curator.data.Image
import space.cherryband.curator.data.RecursiveDirectory

object UserSelectionRepository {
    private val selectedDirs = HashMap<RecursiveDirectory, Int>()
    private val selectedPhotos = HashMap<Image, Int>()
    public const val NO_TAG = Int.MIN_VALUE
    public const val TAG_HIDE = Int.MAX_VALUE

    fun select(dir: RecursiveDirectory, index: Int = 0) {
        selectedDirs[dir] = index
    }
    fun select(image: Image, index: Int = 0) {
        selectedPhotos[image] = index
    }

    fun select(vararg dirs: RecursiveDirectory, index: Int = 0) = dirs.forEach { select(it, index) }
    fun select(vararg images: Image, index: Int = 0) = images.forEach { select(it, index) }

    fun unselect(dir: RecursiveDirectory) {
        selectedDirs.remove(dir)
    }
    fun unselect(image: Image) {
        selectedPhotos.remove(image)
    }

    fun isSelected(dir: RecursiveDirectory) = selectedDirs.containsKey(dir)
    fun isSelected(image: Image) = selectedPhotos.containsKey(image)

    fun tag(dir: RecursiveDirectory) = selectedDirs.getOrElse(dir) { NO_TAG }
    fun tag(image: Image) = selectedPhotos.getOrElse(image) { NO_TAG }

    fun unselect(vararg dirs: RecursiveDirectory) = dirs.forEach { unselect(it) }
    fun unselect(vararg images: Image) = images.forEach { unselect(it) }

    fun dirSelection(): List<RecursiveDirectory> = selectedDirs.keys.toList()
    fun hiddenDirs(): List<RecursiveDirectory> = selectedDirs
        .filterValues { it == TAG_HIDE }
        .keys.toList()
    fun imageSelection(): List<Image> = selectedPhotos.keys.toList()

    fun dirTags(): Map<RecursiveDirectory, Int> = selectedDirs.toMap()
    fun imageTags(): Map<Image, Int> = selectedPhotos.toMap()
}