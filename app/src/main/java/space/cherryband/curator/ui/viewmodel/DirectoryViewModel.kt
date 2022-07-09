package space.cherryband.curator.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.cherryband.curator.data.Path
import space.cherryband.curator.data.repo.UserSelectionRepository
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_EMPTY
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_HIDE
import space.cherryband.curator.util.depth

data class DirectoryViewModel(
    override val path: String,
    val imageCount: MutableLiveData<Int>,
    val sizeTally: MutableLiveData<Long>,
    val tag: MutableLiveData<Int>
 ): ViewModel(), Path {
    val parentTags = arrayOfNulls<MutableLiveData<Int>>(path.depth)
    val activeTag = MediatorLiveData<Int>()

    fun addParentTag(liveTag: MutableLiveData<Int>, index: Int) {
        if (! parentTags.contains(liveTag)) {
            parentTags[index] = liveTag

            activeTag.addSource(liveTag) {
                val newActive = parentTags.indexOfFirst { parent -> parent != null && parent.value != TAG_EMPTY }

                if (newActive < 0)
                    activeTag.value = tag.value
                else
                    activeTag.value = parentTags[newActive]?.value
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DirectoryViewModel

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    fun toggleHide() {
        if (tag.value == TAG_HIDE) tag.value = TAG_EMPTY
        else tag.value = TAG_HIDE
        UserSelectionRepository.setTag(path, tag.value)
    }
}