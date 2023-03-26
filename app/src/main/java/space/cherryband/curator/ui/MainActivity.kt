package space.cherryband.curator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import space.cherryband.curator.ui.compose.MainView
import space.cherryband.curator.ui.theme.CuratorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuratorTheme {
                MainView()
            }
        }
    }
}
