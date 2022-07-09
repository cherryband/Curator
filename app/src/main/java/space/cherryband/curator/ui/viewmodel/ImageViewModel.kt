package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

data class ImageViewModel(
    val id: Long,
    val name: String,
    val path: String,
    val description: String? = null,
    val tag: LiveData<Int>,
): ViewModel()