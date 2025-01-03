package it.reloia.myspotty.spotify.ui

import android.widget.Toast
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.reloia.myspotty.OtherActivity
import it.reloia.myspotty.ui.theme.DarkRed
import me.zhanghai.compose.preference.LocalPreferenceFlow

@Composable
fun SpotifyPage(songId: String, spotifyViewModel: SpotifyViewModel) {
    val context = LocalContext.current
    val preferences by LocalPreferenceFlow.current.collectAsState()

    val password = preferences.get<String>("api_password")
        ?: return (context as OtherActivity).returnToMainWithMessage("No API password set")

    LaunchedEffect(songId) {
        spotifyViewModel.getSongInfo(songId, password)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val url = "https://open.spotify.com/track/${songId}"

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(DarkRed),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val songInfo by spotifyViewModel.songInfo.collectAsState()
        var liked by remember(songInfo) { mutableStateOf(spotifyViewModel.sotd.value.any { it.url.startsWith(url) }) }

        Text(
            (songInfo?.author ?: "No author"),
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    spacing = MarqueeSpacing(80.dp)
                ),
            textAlign = TextAlign.Center
        )
        Text(
            (songInfo?.name ?: "No song playing"),
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    spacing = MarqueeSpacing(80.dp)
                ),
            textAlign = TextAlign.Center
        )
        Text(
            (songInfo?.album_name ?: "No album"),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-6).dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    spacing = MarqueeSpacing(80.dp)
                ),
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier
                .height(22.dp)
        )

        Box(
            modifier = Modifier
                .size(screenWidth * 0.8f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2E2A2A)),
        ) {
            if (songInfo != null) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(songInfo!!.album_image)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Album image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            IconButton(
                onClick = {
                    if (password.isEmpty()) {
                        Toast.makeText(context, "Please set the password in the settings", Toast.LENGTH_SHORT).show()
                        return@IconButton
                    }

                    if (!liked)
                        spotifyViewModel.addToSOTD(url, password)
                    else
                        spotifyViewModel.removeFromSOTD(url, password)

                    liked = !liked
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(36.dp)
                    .background(Color(0xDE190E0E))
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = if (liked) Color.Red else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(33.dp)
        )
    }
}