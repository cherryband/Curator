package space.cherryband.curator.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CallToAction
import androidx.compose.material.icons.twotone.Filter
import androidx.compose.material.icons.twotone.PhotoAlbum
import androidx.compose.ui.graphics.vector.ImageVector
import space.cherryband.curator.R

sealed class Screen(val route: String, @StringRes val descriptionRId: Int, val icon: ImageVector) {
    object Filters: Screen("filters", R.string.tab_filters, Icons.TwoTone.Filter)
    object Selections: Screen("selections", R.string.tab_selections, Icons.TwoTone.PhotoAlbum)
    object Actions: Screen("actions", R.string.tab_actions, Icons.TwoTone.CallToAction)
}