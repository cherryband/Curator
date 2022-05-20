package space.cherryband.curator.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import space.cherryband.curator.data.Image
import space.cherryband.curator.data.viewmodel.PhotosViewModel
import space.cherryband.curator.ui.theme.Shapes
import space.cherryband.curator.util.imageIdToUri


@Composable
fun LivePhotoGrid(rowSize: Int, model: PhotosViewModel = hiltViewModel()){
    val photos = model.getPhotos().observeAsState()

    photos.value?.let {
        if (rowSize == 1)
            PhotoList(photos = it)
        else
            PhotoGrid(rowSize = rowSize, photos = it)
    }
}

@Composable
fun PhotoList(photos: List<Image>) {
    LazyColumn {
        items(photos) { image ->
            SquareAsyncImage(image = image)
        }
    }
}

@Composable
fun LiveDirectoryPhotoGrid(rowSize: Int, model: PhotosViewModel = hiltViewModel()){
    val photos = model.getPhotos().observeAsState()

    photos.value?.let {
        DirectoryPhotoGrid(rowSize = rowSize, photos = it)
    }
}

@Composable
fun DirectoryPhotoGrid(rowSize: Int, photos: List<Image>) {
    val photoDirMap = photos.groupBy { it.path }
    LazyColumn {
        photoDirMap.asIterable().forEach { entry ->
            val dir = entry.key
            val photoRows = entry.value
            item {
                Text(
                    text = dir,
                    modifier = Modifier.padding(16.dp),
                )
            }
            photoGridEmbed(photoRows, rowSize)
        }
    }
}

private fun LazyListScope.photoGridEmbed(
    photos: List<Image>,
    rowSize: Int
) {
    items(photos.chunked(rowSize)) { row ->
        PhotoRow(rowSize, row)
    }
}

@Composable
fun PhotoGrid(rowSize: Int, photos: List<Image>) {
    LazyColumn {
        photoGridEmbed(photos, rowSize)
    }
}

@Composable
fun PhotoRow(rowSize: Int, row: List<Image>) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .aspectRatio(rowSize.toFloat())
            .padding(horizontal = 2.dp, vertical = 1.dp),
    ) {
        row.forEach { image -> SquareAsyncImage(image = image) }
        repeat(rowSize - row.size) {
            Surface(
                modifier = Modifier.aspectRatio(1f),
                color = Color.Transparent
            ) {}
        }
    }
}

@Composable
fun SquareAsyncImage(image: Image) {
    Surface(
        shape = Shapes.medium,
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp),
    ) {
        Box {
            AsyncImage (
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageIdToUri(image.id))
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = image.contentDescription,
                modifier = Modifier.fillMaxSize()
            )
            Text(text = image.name)
        }
    }
}