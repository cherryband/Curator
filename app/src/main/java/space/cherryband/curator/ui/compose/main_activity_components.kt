@file:OptIn(ExperimentalPermissionsApi::class)

package space.cherryband.curator.ui.compose

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.HideImage
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.*
import kotlinx.coroutines.launch
import space.cherryband.curator.R
import space.cherryband.curator.ui.Screen
import space.cherryband.curator.ui.viewmodel.MainViewModel
import space.cherryband.curator.util.randomShrug

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainView(model: MainViewModel = hiltViewModel()){
    val storagePermissionState =
        rememberPermissionState(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        )
    // A surface container using the 'background' color from the theme
    Surface (
        color = MaterialTheme.colors.background
    ) {
        val isEmpty = model.isEmpty.observeAsState()
        when {
            !storagePermissionState.status.isGranted->
                WhatDo(storagePermissionState)
            isEmpty.value == true ->
                NoPhotos()
            else ->
                MainActivityScaffold()
        }
    }
}

@Preview
@Composable
private fun NoPhotos() {
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(Icons.TwoTone.HideImage, contentDescription = null, modifier = Modifier
            .size(240.dp)
            .alpha(0.9f))
        Text(
            stringResource(id = R.string.alert_no_image_available),
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Text(
            stringResource(id = R.string.desc_no_image_available),
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
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
        val title = stringResource(
            if (permissionState.status.shouldShowRationale)
                R.string.error_storage_permission_denied
            else R.string.ask_grant_access_to_storage
        )
        Text(
            title,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )

        Text(
            stringResource(R.string.error_storage_access_rationale),
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

@OptIn(ExperimentalFoundationApi::class)
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
                        Row(modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize(),
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
        HorizontalPager(pageCount = items.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize().padding(it),
            verticalAlignment = Alignment.Bottom,
        ) { page ->
            when (items[page]) {
                Screen.Filters -> RecursiveDirectoryList()
                Screen.Selections ->
                    BoxWithConstraints {
                        if (maxWidth < 480.dp) {
                            LivePhotoGrid(rowSize = 3)
                        } else if (maxWidth < 720.dp) {
                            LivePhotoGrid(rowSize = 4)
                        } else {
                            LivePhotoGrid(rowSize = 6)
                        }
                    }
            }
        }
    }
}