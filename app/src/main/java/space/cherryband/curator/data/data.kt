package space.cherryband.curator.data

data class Image (
    val id: Long,
    val name: String,
    val path: String,
    val size: Long, // in bytes
    val dateModified: Long,
    val contentDescription: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

/**
 * A data class that represents directory
 * The class expects Unix style file path (delimiter "/")
 */
data class Directory (
    override val path: String,
    val imageCount: Int = 0, // non-recursive count of images
    val sizeTally: Long = 0, // non-recursive sum of image.size
): Path {
    infix fun contains(image: Image?) = image?.path == path
    infix fun contains(dir: Directory?) = dir != null && dir.path.startsWith(path)

    override fun hashCode(): Int = path.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Directory

        return path == other.path
    }
}

interface Path {
    val path: String
}