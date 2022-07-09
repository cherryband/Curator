package space.cherryband.curator.domain

import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.*
import space.cherryband.curator.data.Directory
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_EMPTY
import space.cherryband.curator.data.repo.UserSelectionRepository.tag
import space.cherryband.curator.ui.viewmodel.DirectoryViewModel
import space.cherryband.curator.util.*

class DisplayDirectoriesUseCase(
    private val mediaRepo: MediaStoreRepository
    ) {
    private val DELAY_MS = 10 * 1000L
    private val directories = ArrayList<DirectoryViewModel>()

    operator fun invoke(): LiveData<List<DirectoryViewModel>> = liveData {
        if (mediaRepo.update() || directories.isNotEmpty()) {
            mediaRepo.getDirectories().forEach { dir: Directory ->
                val dirVM = directories.firstOrNull { it.path == dir.path }
                if (dirVM != null) {
                    dirVM.apply {
                        imageCount to dir.imageCount
                        sizeTally to dir.sizeTally
                        tag to dir.tag
                    }
                } else {
                    directories += DirectoryViewModel(
                        dir.path,
                        MutableLiveData(dir.imageCount),
                        MutableLiveData(dir.sizeTally),
                        MutableLiveData(dir.tag)
                    )
                    var parents = dir.parents
                    while (parents.isNotEmpty() && directories.none { it.path == parents }) {
                        directories += DirectoryViewModel(
                            parents,
                            MutableLiveData(0),
                            MutableLiveData(0),
                            MutableLiveData(TAG_EMPTY)
                        )
                        parents = parents.parents
                    }
                }
            }

            directories.forEach { dirVM ->
                val childrenVM = directories.filter { it.path.contains(dirVM.path) }
                val depth = dirVM.path.depth
                childrenVM.forEach {
                    it.addParentTag(dirVM.tag, depth - 1)
                }
            }
        }

        emit(directories)
    }

        private infix fun <T> MutableLiveData<T>.to(actualValue: T) {
        if (value != actualValue)
            value = actualValue
    }
}