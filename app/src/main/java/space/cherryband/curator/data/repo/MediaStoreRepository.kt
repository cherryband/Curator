package space.cherryband.curator.data.repo

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import space.cherryband.curator.data.*
import space.cherryband.curator.ui.viewmodel.DirectoryViewModel
import space.cherryband.curator.util.relativeFromStorage

object MediaStoreRepository {
    private val QUERY_SELECTIONS = arrayOf (
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.RELATIVE_PATH
        else
            MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DATE_MODIFIED,
    )

    private var mediaStoreStatus: String? = null
    private var photoList = HashMap<Long, Image>() // Long = _ID
    private var dirList = HashMap<String, Directory>() // String = path
    private lateinit var app: Application

    fun init(app: Application) {
        this.app = app
    }

    fun getPhotos(): List<Image> = photoList.values.toList()
    fun getDirectories(): List<Directory> = dirList.values.toList()

    operator fun get(path: String): Directory? = dirList[path]
    operator fun get(id: Long): Image? = photoList[id]

    fun update(forceUpdate: Boolean = false): Boolean {
        if (mediaStoreStatus != MediaStore.getVersion(app) || forceUpdate) {
            mediaStoreStatus = MediaStore.getVersion(app)

            queryMediaStore(app.contentResolver)
            return true
        }
        return false
    }

    private fun queryMediaStore(resolver: ContentResolver) {
        val query = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resolver.query (
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                QUERY_SELECTIONS,
                null,
                null,
            )
        } else {
            MediaStore.Images.Media.query (
                resolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                QUERY_SELECTIONS
            )
        }
        updateCache(query)
    }

    private fun updateCache(query: Cursor?) {
        query?.use { cursor ->
            val columns = QUERY_SELECTIONS.map { column ->
                cursor.getColumnIndexOrThrow(column)
            }

            while (cursor.moveToNext()) {
                val id = cursor.getLong(columns[0]) // _ID
                val displayName = cursor.getString(columns[1]) // DISPLAY_NAME
                val imgPath = cursor.getString(columns[2]).relativeFromStorage() // RELATIVE_PATH or DATA
                val imgSize = cursor.getLong(columns[3]) // SIZE
                val modifiedTimestamp = cursor.getLong(columns[4]) // DATE_MODIFIED

                photoList[id] = Image(id, displayName, imgPath, imgSize, modifiedTimestamp)
            }
        }

        photoList.values
            .groupBy { it.path }
            .mapValues { entry -> Directory(entry.key, entry.value.size, entry.value.sumOf { it.size }) }
            .toMap(dirList)
    }
}