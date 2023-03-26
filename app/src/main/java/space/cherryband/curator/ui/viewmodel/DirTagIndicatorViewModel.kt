package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import space.cherryband.curator.data.Path

class DirTagIndicatorViewModel(
    override val path: String,
    val tags: List<LiveData<Int>>
): ViewModel(), Path
