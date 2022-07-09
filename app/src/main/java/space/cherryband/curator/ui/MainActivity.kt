@file:OptIn(ExperimentalPagerApi::class, ExperimentalPermissionsApi::class)

package space.cherryband.curator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import space.cherryband.curator.R
import space.cherryband.curator.ui.compose.LivePhotoGrid
import space.cherryband.curator.ui.compose.RecursiveDirectoryList
import space.cherryband.curator.ui.navigation.Screen
import space.cherryband.curator.ui.theme.CuratorTheme
import space.cherryband.curator.util.randomShrug

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val storagePermissionState = rememberPermissionState(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            CuratorTheme {
                // A surface container using the 'background' color from the theme
                Surface (
                    color = MaterialTheme.colors.background
                ) {
                    when {
                        storagePermissionState.hasPermission ->
                            MainActivityScaffold()
                        else ->
                            WhatDo(storagePermissionState)
                    }
                }
            }
        }
    }

}

@Composable
fun WhatDo(permissionState: PermissionState) {
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var shrug by remember { mutableStateOf(randomShrug()) }
        Text(
            text = shrug,
            style = TextStyle(fontSize = 160.sp, fontFamily = FontFamily.SansSerif),
            modifier = Modifier
                .padding(6.dp)
                .clickable { shrug = randomShrug() },
        )
        Text(stringResource(id = R.string.error_storage_permission_denied),
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Text(stringResource(id = R.string.error_storage_access_rationale),
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = { permissionState.launchPermissionRequest() }) {
            Text(stringResource(R.string.button_retry_grant_permission))
        }
    }
}

private val items = listOf(
    Screen.Filters,
    Screen.Selections,
)

@Composable
fun MainActivityScaffold() {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
            ){
                items.forEachIndexed { index, screen ->
                    Tab (selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    ) {
                        Row(modifier = Modifier.padding(4.dp).fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {
                            Icon(screen.icon, stringResource(screen.descriptionRId),
                            modifier = Modifier.padding(6.dp))
                            Text(stringResource(screen.descriptionRId))
                        }
                    }
                }
            }
        } },
    ) {
        HorizontalPager(count = items.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
        ) { page ->
            when (items[page]) {
                Screen.Filters -> RecursiveDirectoryList()
                Screen.Selections -> LivePhotoGrid(rowSize = 3)
            }
        }
    }
}