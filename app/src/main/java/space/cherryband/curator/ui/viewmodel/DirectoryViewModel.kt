package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.cherryband.curator.data.Path
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_EMPTY
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_HIDE
import space.cherryband.curator.data.repo.UserSelectionRepository.setTag

data class DirectoryViewModel(
    override val path: String,
    val imageCount: MutableLiveData<Int>,
    val sizeTally: MutableLiveData<Long>,
    val tag: LiveData<Int>,
 ): ViewModel(), Path {

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

    fun toggleHide(recursive: Boolean = true) {
        if (tag.value == TAG_HIDE) path.setTag(TAG_EMPTY, recursive)
        else path.setTag(TAG_HIDE, recursive)
    }
}