package space.cherryband.curator.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.data.repo.UserSelectionRepository.tag
import space.cherryband.curator.ui.viewmodel.ImageViewModel

class DisplayImagesUseCase(
    private val mediaRepo: MediaStoreRepository
) {
    operator fun invoke(): Flow<List<ImageViewModel>> = mediaRepo.photos.map {
        it.map { img ->
            ImageViewModel(
                img.id,
                img.name,
                img.path,
                img.contentDescription,
                img.id.tag,
                img.path.tag
            )
        }
    }
}