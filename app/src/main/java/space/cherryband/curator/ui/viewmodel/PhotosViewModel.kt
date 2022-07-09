package space.cherryband.curator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import space.cherryband.curator.data.Image
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.domain.DisplayImagesUseCase

class PhotosViewModel: ViewModel(){
    val images = DisplayImagesUseCase()

    fun getPhotos(): LiveData<List<ImageViewModel>> = images()
}