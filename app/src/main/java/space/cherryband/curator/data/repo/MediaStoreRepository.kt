package space.cherryband.curator.data.repo

import android.app.Application
import android.content.ContentResolver
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import space.cherryband.curator.data.*
import space.cherryband.curator.util.getRelativeDirFromStorage

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
    private var rootDir = RootDirectory()
    private lateinit var app: Application

    fun init(app: Application) {
        this.app = app
    }

    fun getPhotos(): List<Image> {
        update()
        return photoList.values.toList()
    }

    fun getImageDirectories(): RootDirectory {
        update()
        return rootDir
    }

    fun update(forceUpdate: Boolean = false) {
        if (mediaStoreStatus != MediaStore.getVersion(app) || forceUpdate){
            mediaStoreStatus = MediaStore.getVersion(app)
            queryMediaStore(app.contentResolver)
        }
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

        rootDir = RootDirectory()

        query?.use { cursor ->
            val columns = QUERY_SELECTIONS.map { column ->
                cursor.getColumnIndexOrThrow(column)
            }

            while (cursor.moveToNext()) {
                val id = cursor.getLong(columns[0]) // _ID
                val displayName = cursor.getString(columns[1]) // DISPLAY_NAME
                val imgPath = getRelativeDirFromStorage(cursor.getString(columns[2])) // RELATIVE_PATH or DATA
                val imgSize = cursor.getLong(columns[3]) // SIZE
                val modifiedTimestamp = cursor.getLong(columns[4]) // DATE_MODIFIED

                if (photoList.containsKey(id)) {
                    photoList[id]?.apply {
                        name = displayName
                        path = imgPath
                        size = imgSize
                        dateModified = modifiedTimestamp
                    }
                } else {
                    photoList[id] = Image(id, displayName, imgPath, imgSize, modifiedTimestamp)
                }

                val dicedPath = dicedPath(imgPath)
                val pathDir = rootDir.find(dicedPath)
                if (pathDir != null) {
                    pathDir.apply { localImageCount ++; sizeTally += imgSize }
                } else {
                    rootDir.add(dicedPath, 1, imgSize)
                }
            }
            cursor.close()
        }
    }
}