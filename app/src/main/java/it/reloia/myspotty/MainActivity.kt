package it.reloia.myspotty

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import it.reloia.myspotty.home.data.remote.MySpottyApiService
import it.reloia.myspotty.home.data.remote.RemoteHomeRepository
import it.reloia.myspotty.home.ui.HomeScreen
import it.reloia.myspotty.home.ui.HomeViewModel
import it.reloia.myspotty.ui.theme.DarkRed
import it.reloia.myspotty.ui.theme.MySpottyTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val systemUiController = rememberSystemUiController()

            systemUiController.setSystemBarsColor(
                color = Color.Transparent
            )

            val homeViewModel = HomeViewModel(
                RemoteHomeRepository(
                    Retrofit.Builder()
                        // TODO: make the base url changeable from the API settings
                        .baseUrl("https://reloia.ddns.net/myspottyapi/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MySpottyApiService::class.java),
                    this
                )
            )

            MySpottyTheme(dynamicColor = false, darkTheme = true) {
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

                            val sheetState = rememberModalBottomSheetState()
                            if (homeViewModel.isSOTDSheetOpen.value) {
                                ModalBottomSheet(
                                    onDismissRequest = {
                                        homeViewModel.toggleSOTDSheet(null)
                                    },
                                    sheetState = sheetState,
                                    containerColor = DarkRed
                                ) {
                                    val currentSOTD = homeViewModel.currentSelectedSOTD.value
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 32.dp)
                                    ) {
                                        Text(
                                            "   " + (currentSOTD?.name ?: "No song selected"),
                                            modifier = Modifier
                                                .basicMarquee(
                                                    iterations = Int.MAX_VALUE,
                                                ),
                                            style = TextStyle(
                                                fontSize = 22.sp,
                                            )
                                        )
                                        Text(
                                            "    " + (currentSOTD?.author ?: "No author"),
                                            modifier = Modifier
                                                .basicMarquee(
                                                    iterations = Int.MAX_VALUE
                                                ),
                                            style = TextStyle(
                                                fontSize = 17.sp,
                                            )
                                        )
                                        Text(
                                            if (currentSOTD != null)
                                                "Added on ${
                                                    SimpleDateFormat(
                                                        "dd/MM/yyyy",
                                                        context.resources.configuration.locales[0]
                                                    ).format(currentSOTD.date)
                                                }"
                                            else "No date",
                                            modifier = Modifier
                                                .padding(start = 16.dp)
                                        )

                                        Button(
                                            onClick = {
                                                val password = context.getSharedPreferences("MySpotty", MODE_PRIVATE)
                                                    .getString("password", null)

                                                if (currentSOTD == null) {
                                                    return@Button
                                                }

                                                if (password == null) {
                                                    return@Button
                                                }

                                                homeViewModel.removeFromSOTD(currentSOTD.date, password)
                                                homeViewModel.toggleSOTDSheet(null)
                                            },
                                            modifier = Modifier
                                                .padding(top = 16.dp)
                                        ) {
                                            Text("Remove from SOTD")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
