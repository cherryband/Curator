package space.cherryband.curator.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object UserSelectionRepository {
    private val selectedDirs = HashMap<String, MutableLiveData<Int>>()
    private val selectedPhotos = HashMap<Long, MutableLiveData<Int>>()

    const val TAG_EMPTY = Int.MAX_VALUE
    const val TAG_HIDE = Int.MIN_VALUE
    val TAG_INHERIT = null
    private val _dirVersion = MutableLiveData(0)

    val String.tag: LiveData<Int>
        @Synchronized
        get() = selectedDirs[this].let { saved ->
            saved ?: MutableLiveData<Int>(TAG_INHERIT).also { new ->
                selectedDirs[this] = new
            }
        }

    val Long.tag: MutableLiveData<Int>
        @Synchronized
        get() = selectedPhotos[this].let { saved ->
            saved ?: MutableLiveData<Int>(TAG_INHERIT).also { new ->
                selectedPhotos[this] = new
            }
        }

    @Synchronized
    fun String.setTag(tag: Int?, recursive: Boolean = false) {
        if (selectedDirs.containsKey(this))
            selectedDirs[this]?.value = tag
        else
            selectedDirs[this] = MutableLiveData(tag)

        if (recursive) {
            selectedDirs
                .filterKeys { it.contains(this) }
                .forEach { (_, selectedTag) ->
                    selectedTag.value = tag
                }
        }

        _dirVersion.value = dirVersion.value?.plus(1) ?: 0
    }

    @Synchronized
    fun Long.setTag(tag: Int?) {
        if (selectedPhotos.containsKey(this))
            selectedPhotos[this]?.value = tag
        else
            selectedPhotos[this] = MutableLiveData(tag)
    }

    val dirVersion: LiveData<Int> get() = _dirVersion
}