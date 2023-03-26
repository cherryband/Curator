package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository
import space.cherryband.curator.domain.DetectUpdateUseCase
import space.cherryband.curator.domain.DisplayDirectoriesUseCase
import space.cherryband.curator.util.isHidden
import space.cherryband.curator.util.recursiveNaturalPath
import javax.inject.Inject

@HiltViewModel
class DirectoriesViewModel @Inject constructor(): ViewModel() {
    private val displayDir = DisplayDirectoriesUseCase(MediaStoreRepository)
    private val excludeHidden = MutableLiveData(true)
    private val updateTick = DetectUpdateUseCase(MediaStoreRepository, UserSelectionRepository)

    fun getDirectories(): LiveData<List<DirectoryViewModel>> = updateTick().switchMap {
        displayDir().map { dirs ->
            if (excludeHidden.value == true)
                dirs
                    .filter { dir -> !dir.tag.isHidden() }
                    .sortedWith(recursiveNaturalPath)
            else dirs.sortedWith(recursiveNaturalPath)
        }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }


    fun setExcludeHidden(bool: Boolean) {
        excludeHidden.value = bool
    }
}