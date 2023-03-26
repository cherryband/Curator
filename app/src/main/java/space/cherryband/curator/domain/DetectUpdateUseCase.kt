package space.cherryband.curator.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository

class DetectUpdateUseCase(
    mediaRepo: MediaStoreRepository,
    selectionRepo: UserSelectionRepository,
) {
    private val updates = MediatorLiveData<Int>()

    init {
        updates.addSource(mediaRepo.version) {
            updates.value = updates.value?.plus(1) ?: it
        }
        updates.addSource(selectionRepo.dirVersion){
            updates.value = updates.value?.plus(1) ?: it
        }
    }
    operator fun invoke(): LiveData<Int> = updates
}