package space.cherryband.curator.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository.tag
import space.cherryband.curator.ui.viewmodel.DirTagIndicatorViewModel
import space.cherryband.curator.util.walk

class DisplayTagIndicatorUseCase(
    private val mediaRepo: MediaStoreRepository
) {
    operator fun invoke(): Flow<Map<String, DirTagIndicatorViewModel>> = mediaRepo.directories.map { list ->
        list.map { dir ->
            DirTagIndicatorViewModel(
                dir.path,
                dir.path.walk
                    .map { it.tag }
            )
        }.associateBy { it.path }
    }
}