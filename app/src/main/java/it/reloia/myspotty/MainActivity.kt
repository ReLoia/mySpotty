package it.reloia.myspotty

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
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
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import it.reloia.myspotty.core.data.api.MySpottyApiService
import it.reloia.myspotty.core.preferences.getApiURL
import it.reloia.myspotty.core.preferences.getOrDefault
import it.reloia.myspotty.home.data.remote.RemoteHomeRepository
import it.reloia.myspotty.home.ui.HomeScreen
import it.reloia.myspotty.home.ui.HomeViewModel
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

        // preferences needed to start the home view model as soon as possible
        val prePreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val baseURL = prePreferences.getApiURL()


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

        val error = intent.getStringExtra("error") ?: ""

        if (error.isNotEmpty()) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        setContent {
            ProvidePreferenceLocals {
                val preferences by LocalPreferenceFlow.current.collectAsState()

                MySpottyTheme(dynamicColor = false, darkTheme = true) {
                    val context = LocalContext.current
                    val configuration = LocalConfiguration.current

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        contentColor = Color.White
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                            topBar = {
                                TopAppBar(
                                    title = {
                                        if (baseURL.isEmpty()) {
                                            Text(stringResource(R.string.api_not_set))
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
                                                contentDescription = getString(R.string.settings),
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
                                                        contentDescription = stringResource(R.string.open_in_browser),
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
                                                stringResource(
                                                    R.string.sotd_infos,
                                                    currentSOTD?.name ?: "",
                                                    currentSOTD?.author ?: "",
                                                    currentSOTD?.url ?: ""
                                                )
                                            )
                                            IconButton(
                                                onClick = {
                                                    context.startActivity(
                                                        Intent.createChooser(
                                                            shareIntent,
                                                            getString(R.string.share)
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
                                                    contentDescription = getString(R.string.share),
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
                                                "  " + (currentSOTD?.name ?: stringResource(R.string.song_not_selected)),
                                                modifier = Modifier
                                                    .basicMarquee(
                                                        iterations = Int.MAX_VALUE,
                                                    ),
                                                fontSize = 22.sp,
                                            )
                                            Text(
                                                "   " + (currentSOTD?.author ?: stringResource(R.string.song_no_author)),
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
                                                            configuration.locales[0]
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
                                                            getString(R.string.sotd_not_found),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        return@Button
                                                    }

                                                    if (password.isEmpty()) {
                                                        Toast.makeText(
                                                            context,
                                                            getString(R.string.no_password_set),
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
                                                Text(stringResource(R.string.sotd_remove))
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
