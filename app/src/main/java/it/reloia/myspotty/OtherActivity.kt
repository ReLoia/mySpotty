package it.reloia.myspotty

import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import it.reloia.myspotty.core.data.api.MySpottyApiService
import it.reloia.myspotty.core.preferences.getApiURL
import it.reloia.myspotty.settings.ui.SettingsAboutPage
import it.reloia.myspotty.settings.ui.SettingsTopBar
import it.reloia.myspotty.spotify.data.remote.RemoteSpotifyRepository
import it.reloia.myspotty.spotify.ui.SpotifyPage
import it.reloia.myspotty.spotify.ui.SpotifyViewModel
import it.reloia.myspotty.ui.theme.DarkRed
import it.reloia.myspotty.ui.theme.MySpottyTheme
import me.zhanghai.compose.preference.ProvidePreferenceFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        val sharedText = intent?.getStringExtra(Intent.EXTRA_TEXT)

        val currentPage =
            if (sharedText != null && sharedText.contains("open.spotify.com", ignoreCase = true)) {
                "spotify"
            } else intent?.getStringExtra("page")
                ?: return returnToMainWithMessage(getString(R.string.no_spotify_url))

        val prePreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val pages = listOf(
            Page(
                name = "Spotify",
                route = "spotify",
                content = {
                    val url = sharedText!!.split(" ").firstOrNull { it.contains("open.spotify.com") }
                        ?: return@Page returnToMainWithMessage(stringResource(R.string.no_spotify_url))

                    val songId = Uri.parse(url).pathSegments.lastOrNull()
                        ?: return@Page returnToMainWithMessage(stringResource(R.string.no_song_id))

                    val baseUrl = prePreferences.getApiURL()

                    val spotifyViewModel = SpotifyViewModel(
                        if (baseUrl.isNotEmpty()) RemoteSpotifyRepository(
                            Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(MySpottyApiService::class.java),
                        ) else null
                    )

                    SpotifyPage(songId, spotifyViewModel)
                },
                topBar = {}
            ),
            Page(
                name = getString(R.string.settings),
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

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            )
        )

        setContent {
            ProvidePreferenceFlow {
                val content = LocalContext.current

                BackHandler {
                    val activityManager = content.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                    val tasks = activityManager.appTasks.firstOrNull()?.taskInfo

                    if (selectedPage.route == "settings" && tasks?.numActivities == 1) {
                        val intent = Intent(content, MainActivity::class.java)
                        startActivity(intent)
                    }

                    finish()
                }

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
                                    },
                                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = DarkRed, scrolledContainerColor = DarkRed)
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

    fun returnToMainWithMessage(message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("error", message)
        startActivity(intent)
        finish()
    }

}