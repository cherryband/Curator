package space.cherryband.curator.domain

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import space.cherryband.curator.data.Directory
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository.tag
import space.cherryband.curator.ui.viewmodel.DirectoryViewModel

class DisplayDirectoriesUseCase (
    private val mediaRepo: MediaStoreRepository
) {

    operator fun invoke(): Flow<List<DirectoryViewModel>> = mediaRepo.directories.map { dirList ->
        dirList.map { dir: Directory ->
            DirectoryViewModel (
                dir.path,
                MutableLiveData(dir.imageCount),
                MutableLiveData(dir.sizeTally),
                dir.path.tag,
            )
        }
    }
}