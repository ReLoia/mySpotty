package it.reloia.myspotty

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import it.reloia.myspotty.home.data.remote.MySpottyApiService
import it.reloia.myspotty.home.data.remote.RemoteHomeRepository
import it.reloia.myspotty.home.ui.HomeScreen
import it.reloia.myspotty.home.ui.HomeViewModel
import it.reloia.myspotty.ui.theme.DarkRed
import it.reloia.myspotty.ui.theme.MySpottyTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContent {
            val homeViewModel = HomeViewModel(
                RemoteHomeRepository(
                    Retrofit.Builder()
                        // TODO: make the base url changeable from the API settings
                        .baseUrl("https://reloia.ddns.net/reloia_listen/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MySpottyApiService::class.java),
                    this
                )
            )

            MySpottyTheme(dynamicColor = false) {
                val context = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = Color.White
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                // TODO: make the api name changeable from the API settings
                                title = {
                                    Text("no api set")
                                },
                                actions = {
                                    IconButton(onClick = {
                                        val intent = Intent(context, OtherActivity::class.java)
                                        intent.putExtra("page", "settings")
                                        context.startActivity(intent)
                                    }) {
                                        Icon(
                                            Icons.Default.Settings,
                                            contentDescription = "Settings",
                                            tint = Color.White
                                        )

                                    }
                                },
                                colors = topAppBarColors(
                                    containerColor = DarkRed.copy(alpha = 0.3f),
                                    titleContentColor = Color.White
                                )
                            )
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            HomeScreen(innerPadding, homeViewModel)
                        }
                    }
                }
            }
        }
    }
}
