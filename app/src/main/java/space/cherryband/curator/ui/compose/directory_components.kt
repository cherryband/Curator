package space.cherryband.curator.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.cherryband.curator.data.RecursiveDirectory
import space.cherryband.curator.data.RootDirectory
import space.cherryband.curator.data.repo.UserSelectionRepository

@Composable
fun RecursiveDirectoryList(dirs: RootDirectory) {
    LazyColumn{
        dirs.children.sortedBy { it.name }.forEach {
            recursiveDirectoryEmbed(it)
        }
        //items(dirs.children.sortedBy { it.name }){
        //    RecursiveDirectoryComponent(dir = it)
        //}
    }
}

private fun LazyListScope.recursiveDirectoryEmbed(
    rootDir: RecursiveDirectory
) {
    item {
        val childExpanded = remember { mutableStateOf(false) }
        RecursiveDirectoryComponent(dir = rootDir,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                childExpanded.value = !childExpanded.value
            },)
        if (childExpanded.value) {
            UserSelectionRepository.select(rootDir, UserSelectionRepository.NO_TAG)
            rootDir.children.forEach {
                this@recursiveDirectoryEmbed.recursiveDirectoryEmbed(it)
            }
        } else
            UserSelectionRepository.select(rootDir, UserSelectionRepository.TAG_HIDE)
    }
}

@Composable
fun RecursiveDirectoryComponent (dir: RecursiveDirectory, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier) {

        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp),
        ){
            for (i in 0 until dir.depth){
                Spacer(modifier = Modifier.padding(4.dp))
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(4.dp),
                    color = if (i == dir.depth - 1) Color.White else Color.Gray,
                ) {}
            }
            Icon(
                Icons.TwoTone.Folder,
                null,
                modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp)
            )
            Text(text = dir.name,
                modifier = Modifier.padding(4.dp))
        }

        Text(text = "${dir.localImageCount}" +
                if (dir.children.isNotEmpty())
                    "/${dir.imageCount}"
                else "",
            modifier = Modifier.padding(4.dp))
    }
    //if (expanded.value){
    //    dir.children.forEach {
    //        RecursiveDirectoryComponent(dir = it)
    //    }
    //}
}

fun dirNameHighlighted(dir: RecursiveDirectory): AnnotatedString {
    return with(AnnotatedString.Builder()){
        pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
        pushStyle(SpanStyle(fontWeight = FontWeight.Light))
        dir.parent?.apply { append(path) }
        pop()
        pushStyle(SpanStyle(fontWeight = FontWeight.SemiBold))
        append(dir.name)
        toAnnotatedString()
    }
}

@Preview
@Composable
fun DirectoryPreview() {
    RecursiveDirectoryComponent(
        RecursiveDirectory(null,"Screenshots", 10, 54613)
    )
}