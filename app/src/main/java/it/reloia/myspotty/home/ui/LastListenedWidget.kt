package it.reloia.myspotty.home.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
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
import it.reloia.myspotty.home.domain.model.LastListened

@Composable
fun LastListenedWidget(lastListened: LastListened?, viewModel: HomeViewModel) {
    // TODO: make the value dynamic and not hardcoded, also make it a separate composable, also hide it if the song on top is not playing (aka. currentSong == lastSong)
    Text(
        "last listened song",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(bottom = 6.dp)
    )

    Text(
        (lastListened?.author ?: "No author"),
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                spacing = MarqueeSpacing(80.dp)
            ),
        textAlign = TextAlign.Center
    )
    Text(
        (lastListened?.name ?: "No song playing"),
        fontSize = 19.sp,
        modifier = Modifier
            .fillMaxWidth()
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                spacing = MarqueeSpacing(80.dp)
            ),
        textAlign = TextAlign.Center
    )
    Text(
        (lastListened?.album_name ?: "No album"),
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-6).dp)
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                spacing = MarqueeSpacing(80.dp)
            ),
        textAlign = TextAlign.Center
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var liked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(screenWidth * 0.74f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2E2A2A)),
    ) {
        if (lastListened != null) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(lastListened.album_image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Album image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        val context = LocalContext.current

        IconButton(
            onClick = {
                val password = context.getSharedPreferences("MySpotty", Context.MODE_PRIVATE)
                    .getString("password", null)

                if (lastListened == null) {
                    Toast.makeText(context, "No song playing", Toast.LENGTH_SHORT).show()
                    return@IconButton
                }

                if (password == null) {
                    Toast.makeText(context, "Please set the password in the settings", Toast.LENGTH_SHORT).show()
                    return@IconButton
                }

                if (liked)
                    viewModel.addToSOTD(lastListened.song_link, password)
                else
                    viewModel.removeFromSOTD(lastListened.song_link, password)

                liked = !liked
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .clip(CircleShape)
                .size(34.dp)
                .background(Color(0xDE190E0E))
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = if (liked) Color.Red else Color.White,
                modifier = Modifier.size(19.dp)
            )
        }
    }

    Spacer(
        modifier = Modifier
            .height(26.dp)
    )
}