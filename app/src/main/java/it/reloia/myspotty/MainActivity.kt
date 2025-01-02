package it.reloia.myspotty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import it.reloia.myspotty.home.data.remote.MySpottyApiService
import it.reloia.myspotty.home.data.remote.RemoteHomeRepository
import it.reloia.myspotty.home.ui.HomeScreen
import it.reloia.myspotty.home.ui.HomeViewModel
import it.reloia.myspotty.model.getOrDefault
import it.reloia.myspotty.ui.theme.DarkRed
import it.reloia.myspotty.ui.theme.MySpottyTheme
import me.zhanghai.compose.preference.LocalPreferenceFlow
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class MainActivity : ComponentActivity() {
    private lateinit var homeViewModel: HomeViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // preferences needed to start the home view model as soon as possible
        @Suppress("DEPRECATION")
        val prePreferences = getDefaultSharedPreferences(this)
        val baseURL = (prePreferences.getString("api_url", "") ?: "").let {
            if (it.isEmpty()) return@let it
            if (!it.endsWith("/")) "$it/" else it
        }

        homeViewModel = HomeViewModel(
            if (baseURL.isNotEmpty()) RemoteHomeRepository(
                Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MySpottyApiService::class.java),
                baseURL
            ) else null
        )


        setContent {
            ProvidePreferenceLocals {
                val systemUiController = rememberSystemUiController()
                val preferences by LocalPreferenceFlow.current.collectAsState()

                systemUiController.setSystemBarsColor(
                    color = Color.Transparent
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
                                    title = {
                                        if (baseURL.isEmpty()) {
                                            Text("no api set")
                                        }
                                    },
                                    actions = {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (homeViewModel.isWebSocketConnected.value)
                                                        Color(0xFF00FF00)
                                                    else
                                                        Color(0xFFFF0000)
                                                )
                                        )

                                        IconButton(onClick = {
                                            val intent = Intent(context, OtherActivity::class.java)
                                            intent.putExtra("page", "settings")
                                            context.startActivity(intent)
                                            finish()
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
                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            if (currentSOTD?.url != null) {
                                                val browserIntent = Intent(Intent.ACTION_VIEW)
                                                browserIntent.data = Uri.parse(currentSOTD.url)
                                                IconButton(
                                                    onClick = {
                                                        context.startActivity(browserIntent)
                                                    },
                                                    modifier = Modifier
                                                        .padding(end = 10.dp)
                                                        .clip(CircleShape)
                                                        .size(28.dp)
                                                        .background(Color(0xFF915B1A))
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.outline_globe_24),
                                                        contentDescription = "Open in Browser",
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(19.dp)
                                                    )
                                                }
                                            }

                                            val shareIntent = Intent(Intent.ACTION_SEND)
                                            shareIntent.type = "text/plain"
                                            shareIntent.putExtra(
                                                Intent.EXTRA_TEXT,
                                                "${currentSOTD?.name} by ${currentSOTD?.author}: ${currentSOTD?.url}"
                                            )
                                            IconButton(
                                                onClick = {
                                                    context.startActivity(
                                                        Intent.createChooser(
                                                            shareIntent,
                                                            "Share"
                                                        )
                                                    )
                                                },
                                                modifier = Modifier
                                                    .padding(end = 10.dp)
                                                    .clip(CircleShape)
                                                    .size(28.dp)
                                                    .background(Color(0xFF1A7591))
                                                    .padding(end = 4.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Share,
                                                    contentDescription = "Share",
                                                    tint = Color.White,
                                                    modifier = Modifier
                                                        .size(19.dp)
                                                )
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 32.dp)
                                        ) {
                                            Text(
                                                "  " + (currentSOTD?.name ?: "No song selected"),
                                                modifier = Modifier
                                                    .basicMarquee(
                                                        iterations = Int.MAX_VALUE,
                                                    ),
                                                fontSize = 22.sp,
                                            )
                                            Text(
                                                "   " + (currentSOTD?.author ?: "No author"),
                                                modifier = Modifier
                                                    .basicMarquee(
                                                        iterations = Int.MAX_VALUE
                                                    ),
                                                fontSize = 16.sp
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
                                                    .padding(start = 12.dp),
                                                fontSize = 15.sp
                                            )

                                            Button(
                                                onClick = {
                                                    val password = preferences.getOrDefault("api_password", "")

                                                    if (currentSOTD == null) {
                                                        Toast.makeText(
                                                            context,
                                                            "Cannot find the SOTD",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        return@Button
                                                    }

                                                    if (password.isEmpty()) {
                                                        Toast.makeText(
                                                            context,
                                                            "Please set the password in the settings",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        return@Button
                                                    }

                                                    homeViewModel.removeFromSOTD(
                                                        currentSOTD.date,
                                                        password
                                                    )
                                                    homeViewModel.toggleSOTDSheet(null)
                                                },
                                                modifier = Modifier
                                                    .padding(top = 16.dp)
                                                    .align(Alignment.CenterHorizontally),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xDE9F1414)
                                                )
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

    override fun onPause() {
        super.onPause()
        homeViewModel.stopWebSocket()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.startWebSocket()
    }
}
