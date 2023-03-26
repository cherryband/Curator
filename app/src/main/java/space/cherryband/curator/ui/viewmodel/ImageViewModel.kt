package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_INHERIT

data class ImageViewModel(
    val id: Long,
    val name: String,
    val path: String,
    val description: String? = null,
    val tag: LiveData<Int>,
    val parentTag: LiveData<Int>,
): ViewModel() {
    val activeTag get() = if (tag.value == TAG_INHERIT) parentTag else tag
}