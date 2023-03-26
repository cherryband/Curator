package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository
import space.cherryband.curator.domain.DetectUpdateUseCase
import space.cherryband.curator.domain.DisplayTagIndicatorUseCase
import javax.inject.Inject

@HiltViewModel
class TagIndicatorsViewModel @Inject constructor(): ViewModel() {
    private val updateTick = DetectUpdateUseCase(MediaStoreRepository, UserSelectionRepository)
    private val displayTagIndicator = DisplayTagIndicatorUseCase(MediaStoreRepository)

    fun getTagIndicators(): LiveData<Map<String, DirTagIndicatorViewModel>> = updateTick().switchMap {
        displayTagIndicator().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }
}