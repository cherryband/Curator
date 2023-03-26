package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.cherryband.curator.data.repo.MediaStoreRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    val isEmpty: LiveData<Boolean> = MediaStoreRepository.isEmpty
}