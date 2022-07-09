package space.cherryband.curator.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.domain.DisplayDirectoriesUseCase
import space.cherryband.curator.util.isHidden
import space.cherryband.curator.util.recursiveNaturalPath

class DirectoriesViewModel: ViewModel() {
    private val displayDir = DisplayDirectoriesUseCase(MediaStoreRepository)
    private val excludeHidden = MutableLiveData(true)
    fun getDirectories(): LiveData<List<DirectoryViewModel>> = Transformations.switchMap(excludeHidden) {
        displayDir.invoke().map { dirs ->
            if(excludeHidden.value == true)
            dirs
                .filter { dir -> dir.parentTags.none { it != dir.tag && it?.value.isHidden() } }
                .sortedWith(recursiveNaturalPath)
            else dirs.sortedWith(recursiveNaturalPath)
        }
    }

    fun setExcludeHidden(bool: Boolean) {
        excludeHidden.value = bool
    }
}