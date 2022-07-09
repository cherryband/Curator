package space.cherryband.curator.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_EMPTY
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_HIDE
import space.cherryband.curator.ui.viewmodel.DirectoriesViewModel
import space.cherryband.curator.ui.viewmodel.DirectoryViewModel
import space.cherryband.curator.util.diced
import space.cherryband.curator.util.isHidden
import space.cherryband.curator.util.leaf
import space.cherryband.curator.util.parents

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecursiveDirectoryList(dirVM: DirectoriesViewModel = hiltViewModel()) {
    val dirs = dirVM.getDirectories().observeAsState()
    LazyColumn {
        dirs.value?.let {
            items(it) { dir ->
                Surface(color = MaterialTheme.colors.background,
                    modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(onClick = {
                    }, onLongClick = {
                        dir.toggleHide()
                        dirVM.setExcludeHidden(true)
                    })
                ) {
                    DirectoryComponent(dir)
                }
            }
        }
    }
}

@Composable
fun DirectoryComponent (dir: DirectoryViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp),
        ) {
            RecursiveTagIndicator(dir)
            Icon(
                Icons.TwoTone.Folder,
                null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp)
            )
            Text(text = dirNameHighlighted(dir),
                modifier = Modifier.padding(4.dp))
        }

        dir.imageCount.observeAsState().value?.let {
            Text(text = "$it", modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun RecursiveTagIndicator(dir: DirectoryViewModel) {
    dir.parentTags.forEach {
        val tag = it?.observeAsState()
        Spacer(modifier = Modifier.padding(4.dp))
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(8.dp)
                .alpha(if (it == dir.tag) 1f else 0.7f),
            border = BorderStroke(1.5.dp, if (it == dir.tag) Color.White else Color.LightGray),
            color = if (tag?.value.isHidden()) Color.Transparent else if (it == dir.tag) Color.White else Color.LightGray,
        ) {}
    }
}

@Composable
fun dirNameHighlighted(dir: DirectoryViewModel): AnnotatedString {
    val tag = dir.activeTag.observeAsState()
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
}