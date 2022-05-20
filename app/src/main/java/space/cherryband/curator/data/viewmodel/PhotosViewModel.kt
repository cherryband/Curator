package space.cherryband.curator.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import space.cherryband.curator.data.Image
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository

class PhotosViewModel: ViewModel(){
    private val photos: MutableLiveData<List<Image>> by lazy {
        MutableLiveData<List<Image>>().also {
            it.value = MediaStoreRepository.getPhotos()
        }
    }

    fun getPhotos(): LiveData<List<Image>> = Transformations.map(photos) { images ->
        UserSelectionRepository.hiddenDirs()
            .let { hiddenDirs ->
                images.filter { image -> !(image isIn hiddenDirs) }
                    .toList()
            }
    }
}