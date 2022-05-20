package space.cherryband.curator.data

data class Image (
    val id: Long,
    var name: String,
    var path: String,
    var size: Long, // in bytes
    var dateModified: Long,
    var contentDescription: String? = null
){
    infix fun isIn(dirs: Collection<RecursiveDirectory>) = dirs.any { dir -> dir contains this }
    //infix fun isIn(dir: RecursiveDirectory) = dir contains this
}

data class RootDirectory(
    val name: String? = null,
){
    val children = ArrayList<RecursiveDirectory>()

    override fun toString(): String {
        return "RootDirectory: " +
                "name = $name, " +
                if (children.isNotEmpty())
                    "${children.size} immediate children:${children.joinToString { " \"${it.name}\"" }}."
                else "no children."
    }

    fun add(dicedPath: List<String>, imageCount: Int = 0, sizeTally: Long = 0): RecursiveDirectory? {
        if (dicedPath.isEmpty()) return null
        var parentChild = children.firstOrNull { child -> child.name == dicedPath[0] }
        if (parentChild == null){
            parentChild = RecursiveDirectory(null, dicedPath[0], imageCount, sizeTally)
            children.add(parentChild)
        }

        return if (dicedPath.size == 1) parentChild
        else parentChild.add(dicedPath, imageCount, sizeTally)
    }

    /**
     * finds the directory object for given path.
     * @param dicedPath: a UNIX target path(absolute/relative) for requesting object of.
     * @return RecursiveDirectory object for given path, or null if not found.
     */
    fun find(dicedPath: List<String>): RecursiveDirectory? {
        return if (dicedPath.isEmpty()) null
        else {
            for (child in children) {
                val result = child.find(dicedPath)
                if (result != null) return result
            }
            return null
        }
    }
}

/**
 * A doubly-linked tree representing directory structure
 * The class expects Unix style file path (delimiter "/").
 */

data class RecursiveDirectory(
    val parent: RecursiveDirectory?,
    val name: String,
    var localImageCount: Int = 0,
    var sizeTally: Long = 0,
    val children: MutableSet<RecursiveDirectory> = HashSet(),
) {
    val depth: Int = (parent?.depth?.plus(1)) ?: 1
    val path: String = (parent?.path?.plus("$name/")) ?: "$name/"
    val imageCount: Int = localImageCount
        get() = localImageCount + children.fold(field){ acc, dir -> acc + dir.imageCount }

    init {
        parent?.children?.add(this)
    }

    override fun toString(): String {
        return "RecursiveDirectory: " +
                "path = $path, " +
                "imageCount = $imageCount, " +
                "sizeTally = $sizeTally, " +
                if (children.isNotEmpty())
                    "${children.size} immediate children:${children.joinToString { " \"${it.name}\"" }}."
                else "no children."
    }

    fun add(dicedPath: List<String>, imageCount: Int = 0, sizeTally: Long = 0): RecursiveDirectory? {
        if (dicedPath.isEmpty()) return null
        var parentChild: RecursiveDirectory? = null
        if (dicedPath.size > 1) {
            parentChild = children.firstOrNull { child -> dicedPath[1] == child.name }
        }
        if (parentChild == null) {
            parentChild = RecursiveDirectory(this, dicedPath[1])
        }

        return when (dicedPath.size) {
            2 -> parentChild.apply { localImageCount = imageCount; this.sizeTally = sizeTally }
            1 -> this
            else -> parentChild.add(dicedPath.drop(1), imageCount, sizeTally)
        }
    }

    /**
     * finds the directory object for given path.
     * @param children: a UNIX target path(absolute/relative) for requesting object of.
     * @return RecursiveDirectory object for given path, or null if not found.
     */
    fun find(children: List<String>): RecursiveDirectory? {
        return if (children.isEmpty() || name != children[0]) null
        else if (children.size == 1) this
        else {
            for (child in this.children) {
                val result = child.find(children.drop(1))
                if (result != null) return result
            }
            return null
        }
    }

    infix fun contains(image: Image) = image.path == path
    infix fun contains(dir: RecursiveDirectory) = dir.path.startsWith(path)
}

fun dicedPath(path: String): List<String> = path.trimEnd('/').split('/')
