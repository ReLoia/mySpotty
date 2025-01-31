package it.reloia.myspotty.home.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.reloia.myspotty.R
import it.reloia.myspotty.core.domain.model.CurrentSong
import it.reloia.myspotty.core.preferences.getOrDefault
import me.zhanghai.compose.preference.LocalPreferenceFlow

@Composable
fun ListeningWidget(currentSong: CurrentSong?, viewModel: HomeViewModel) {
    val preferences by LocalPreferenceFlow.current.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var liked by remember(currentSong)  { mutableStateOf(viewModel.sotd.value.any { it.url == currentSong?.song_link }) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(2.dp)
        )

        Box(
            modifier = Modifier
                .size(screenWidth * 0.8f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2E2A2A)),
        ) {
            if (currentSong != null) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(currentSong.album_image)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.song_album_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            val context = LocalContext.current

            IconButton(
                onClick = {
                    val password = preferences.getOrDefault("api_password", "")

                    if (currentSong == null) {
                        Toast.makeText(context,
                            context.getString(R.string.song_not_playing), Toast.LENGTH_SHORT).show()
                        return@IconButton
                    }

                    if (password.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.no_password_set), Toast.LENGTH_SHORT).show()
                        return@IconButton
                    }

                    if (!liked)
                        viewModel.addToSOTD(currentSong.song_link, password)
                    else
                        viewModel.removeFromSOTD(currentSong.song_link, password)

                    liked = !liked
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(32.dp)
                    .background(Color(0xDE190E0E))
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.favorite),
                    tint = if (liked) Color.Red else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (currentSong == null) return

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        val clampedProgress = if (viewModel.progress.value > 0) viewModel.progress.value / currentSong.duration.toFloat() else 1f

        Box(
            modifier = Modifier
                .height(8.dp)
                .width(screenWidth * 0.7f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2E2A2A))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(clampedProgress)
                    .background(if (currentSong.playing) Color.White else Color.Gray)
            )
        }
    }
}