package space.cherryband.curator.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import space.cherryband.curator.ui.viewmodel.DirTagIndicatorViewModel
import space.cherryband.curator.ui.viewmodel.DirectoriesViewModel
import space.cherryband.curator.ui.viewmodel.DirectoryViewModel
import space.cherryband.curator.ui.viewmodel.TagIndicatorsViewModel
import space.cherryband.curator.util.isHidden
import space.cherryband.curator.util.leaf
import space.cherryband.curator.util.parents

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecursiveDirectoryList(dirVM: DirectoriesViewModel = hiltViewModel(),
                           indicators: TagIndicatorsViewModel = hiltViewModel()) {
    val dirs = dirVM.getDirectories().observeAsState()
    val tags = indicators.getTagIndicators().observeAsState()
    LazyColumn {
        dirs.value?.let {
            items(it) { dir ->
                Surface (
                    color = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                dir.toggleHide(false)
                                dirVM.setExcludeHidden(false)
                            },
                            onLongClick = {
                                dir.toggleHide(true)
                                dirVM.setExcludeHidden(false)
                            }
                        )
                ) {
                    DirectoryComponent(dir, tags.value?.get(dir.path))
                }
            }
        }
    }
}

@Composable
fun DirectoryComponent (dir: DirectoryViewModel, indicator: DirTagIndicatorViewModel?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        indicator?.let { RecursiveTagIndicator(it) }
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f),
        ) {
            Icon (
                Icons.TwoTone.Folder,
                null,
                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
            )
            Text (
                text = dirNameHighlighted(dir),
                modifier = Modifier.padding(4.dp)
            )
        }

        dir.imageCount.observeAsState().value?.let {
            Text(text = "$it", modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun RecursiveTagIndicator(indicator: DirTagIndicatorViewModel) {
    indicator.tags.forEachIndexed { index, liveTag ->
        val tag = liveTag.observeAsState()
        val isLeaf = index == indicator.tags.size - 1
        Spacer(modifier = Modifier.padding(4.dp))
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(8.dp)
                .alpha(if (isLeaf) 1f else 0.7f),
            border = BorderStroke(1.5.dp, if (isLeaf) Color.White else Color.LightGray),
            color = if (tag.value.isHidden()) Color.Transparent else if (isLeaf) Color.White else Color.LightGray,
        ) {}
    }
}

@Composable
fun dirNameHighlighted(dir: DirectoryViewModel): AnnotatedString {
    val tag = dir.tag.observeAsState()
    return buildAnnotatedString {
        pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
        if (tag.value.isHidden())
            pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
        pushStyle(SpanStyle(fontWeight = FontWeight.Light, color = Color.Gray))
        if (dir.path.parents.isNotEmpty())
            append(dir.path.parents + "/")
        pop()
        if (!tag.value.isHidden())
            pushStyle(SpanStyle(fontWeight = FontWeight.SemiBold))
        append(dir.path.leaf)
    }
}

/*
@Preview
@Composable
fun DirectoryPreview() {
    val empty = MutableLiveData(TAG_EMPTY)
    val hide = MutableLiveData(TAG_HIDE)
    Column (
        modifier = Modifier.background(Color.White)
    ){
        DirectoryComponent(
            DirectoryViewModel(
                "DCIM/Screenshots",
                MutableLiveData(10),
                MutableLiveData(54613),
                empty
            )
        )
        DirectoryComponent(
            DirectoryViewModel(
                "DCIM/Camera",
                MutableLiveData(50),
                MutableLiveData(589453),
                hide
            )
        )
        DirectoryComponent(
            DirectoryViewModel(
                "Photos/Motion Picture/",
                MutableLiveData(1),
                MutableLiveData(270),
                MutableLiveData(TAG_EMPTY),
            )
        )
        DirectoryComponent(
            DirectoryViewModel(
                "Music/Monstercat/Gaming/Rocket League x Monstercat Vol. 5/Castaway",
                MutableLiveData(1),
                MutableLiveData(2400),
                MutableLiveData(TAG_EMPTY),
            )
        )
    }
} */