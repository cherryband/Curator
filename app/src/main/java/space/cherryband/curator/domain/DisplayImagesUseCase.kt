package space.cherryband.curator.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import space.cherryband.curator.ui.viewmodel.ImageViewModel

class DisplayImagesUseCase {
    operator fun invoke(): LiveData<List<ImageViewModel>> = liveData {

    }
}