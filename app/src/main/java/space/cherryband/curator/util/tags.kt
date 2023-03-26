package space.cherryband.curator.util

import androidx.lifecycle.LiveData
import space.cherryband.curator.data.repo.UserSelectionRepository.TAG_HIDE

fun Int?.isHidden() = this == TAG_HIDE
fun LiveData<Int>?.isHidden() = this?.value?.isHidden() == true