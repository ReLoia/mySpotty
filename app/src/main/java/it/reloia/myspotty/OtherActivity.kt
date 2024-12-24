package it.reloia.myspotty

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import it.reloia.myspotty.ui.theme.MySpottyTheme

/**
 * TODO: handle more pages with a class for "pages" and create a list of pages to switch between using `currentPage` variable
 */
class OtherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currentPage = "Test"

        val sharedText = intent?.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null && sharedText.contains("open.spotify.com", ignoreCase = true)) {
            currentPage = "spotify"
        }

        println("OtherActivity: $currentPage ; $sharedText")

        enableEdgeToEdge()
        setContent {
            val systemUiController = rememberSystemUiController()

            systemUiController.setSystemBarsColor(
                color = Color.Transparent
            )

            MySpottyTheme (dynamicColor = false, darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = "Hello, OtherActivity!",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}