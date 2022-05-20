package space.cherryband.curator.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import space.cherryband.curator.data.RootDirectory
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository

class DirectoriesViewModel: ViewModel(){
    private val directories: MutableLiveData<RootDirectory> by lazy {
        MutableLiveData<RootDirectory>().also {
            it.value = MediaStoreRepository.getImageDirectories()
        }
    }

    fun getDirectories(): LiveData<RootDirectory> = Transformations.map(directories) { root ->
        root.also {
            UserSelectionRepository.hiddenDirs()
                .forEach{

                }
        }
    }
}