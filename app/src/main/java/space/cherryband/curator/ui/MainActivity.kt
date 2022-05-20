package space.cherryband.curator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import space.cherryband.curator.data.repo.MediaStoreRepository
import space.cherryband.curator.ui.compose.RecursiveDirectoryList
import space.cherryband.curator.ui.compose.LivePhotoGrid
import space.cherryband.curator.ui.navigation.Screen
import space.cherryband.curator.ui.theme.CuratorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuratorTheme {
                // A surface container using the 'background' color from the theme
                Surface (
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    MainActivityScaffold(navController)
                }
            }
        }
    }
}

private val items = listOf(
    Screen.Filters,
    Screen.Selections,
    //Screen.Actions,
)

@Composable
fun MainActivityScaffold(navController: NavHostController) {
    Scaffold(
        bottomBar = { MainNavigation(navController) }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Filters.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Filters.route) { RecursiveDirectoryList(dirs = MediaStoreRepository.getImageDirectories()) }
            composable(Screen.Selections.route) { LivePhotoGrid(rowSize = 3) }
        }
    }
}

@Composable
private fun MainNavigation(navController: NavHostController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem (
                icon = { Icon(screen.icon, stringResource(screen.descriptionRId)) },
                label = { Text(stringResource(screen.descriptionRId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}