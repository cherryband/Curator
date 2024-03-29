package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository
import space.cherryband.curator.domain.DetectUpdateUseCase
import space.cherryband.curator.domain.DisplayImagesUseCase
import space.cherryband.curator.util.isHidden
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(): ViewModel() {
    val images = DisplayImagesUseCase(MediaStoreRepository)
    private val updateTick = DetectUpdateUseCase(MediaStoreRepository, UserSelectionRepository)

    fun getPhotos(): LiveData<List<ImageViewModel>> = updateTick().switchMap {
        images().map {
            it.filter { img -> !img.activeTag.isHidden() }
        }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }
}