package space.cherryband.curator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import space.cherryband.curator.data.repo.MediaStoreRepository

@HiltAndroidApp
class Curator: Application() {
    override fun onCreate() {
        super.onCreate()
        MediaStoreRepository.init(this)
    }
}