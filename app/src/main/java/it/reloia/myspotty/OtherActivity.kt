package it.reloia.myspotty

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import it.reloia.myspotty.settings.ui.SettingsAboutPage
import it.reloia.myspotty.settings.ui.SettingsTopBar
import it.reloia.myspotty.ui.theme.MySpottyTheme
import me.zhanghai.compose.preference.ProvidePreferenceFlow

data class Page(
    val name: String,
    val route: String,
    val content: @Composable () -> Unit,
    val topBar: @Composable (() -> Unit)? = null
)

class OtherActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currentPage = "test"

        val extraPage = intent?.getStringExtra("page")

        val sharedText = intent?.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null && sharedText.contains("open.spotify.com", ignoreCase = true)) {
            currentPage = "spotify"
        } else if (extraPage != null) {
            currentPage = extraPage
        }

        val pages = listOf(
            Page(
                name = "Spotify",
                route = "spotify",
                content = {
                    Text("Hello, Spotify!")
                }
            ),
            Page(
                name = "Settings",
                route = "settings",
                content = { SettingsAboutPage() },
                topBar = { SettingsTopBar() }
            ),
            Page(
                name = "test",
                route = "test",
                content = {
                    Text("Hello, OtherActivity!")
                }
            )
        )

        val selectedPage = pages.first { it.route == currentPage }

        enableEdgeToEdge()
        setContent {
            ProvidePreferenceFlow {


                val systemUiController = rememberSystemUiController()

                systemUiController.setSystemBarsColor(
                    color = Color.Transparent
                )

                MySpottyTheme(dynamicColor = false, darkTheme = true) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            if (selectedPage.topBar != null) {
                                selectedPage.topBar.invoke()
                            } else {
                                TopAppBar(
                                    title = {
                                        Text(selectedPage.name)
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            selectedPage.content()
                        }
                    }
                }
            }
        }
    }
}