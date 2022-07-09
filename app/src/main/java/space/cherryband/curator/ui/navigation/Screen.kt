package space.cherryband.curator.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CallToAction
import androidx.compose.material.icons.twotone.Filter
import androidx.compose.material.icons.twotone.PhotoAlbum
import androidx.compose.ui.graphics.vector.ImageVector
import space.cherryband.curator.R

sealed class Screen(@StringRes val descriptionRId: Int, val icon: ImageVector) {
    object Filters: Screen(R.string.tab_directories, Icons.TwoTone.Filter)
    object Selections: Screen(R.string.tab_pictures, Icons.TwoTone.PhotoAlbum)
}