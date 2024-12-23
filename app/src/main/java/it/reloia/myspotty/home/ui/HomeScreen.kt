package it.reloia.myspotty.home.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(paddingValues: PaddingValues, homeViewModel: HomeViewModel) {
    val currentSong = homeViewModel.currentSong.collectAsState().value
    val sotd = homeViewModel.sotd.collectAsState().value

    println("HomeScreen) current song loaded")

    // TODO: complete the pull to refresh box logic
    PullToRefreshBox (
        onRefresh = {
            homeViewModel.refresh()
        },
        isRefreshing = homeViewModel.isRefreshing,
        modifier = Modifier
            .padding(top = 20.dp)
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )

//            AutoScrollingMarqueeText(
//                currentSong?.name ?: "No song playing",
//                modifier = Modifier
//                    .padding(horizontal = 14.dp),
//                fontSize = 24.sp,
//                color = Color.White
//            )
            Text(
                "  " + (currentSong?.name ?: "No song playing"),
                fontSize = 24.sp,
                modifier = Modifier
//                    .padding(start = 14.dp)
                    .basicMarquee()
            )
            Text(
                currentSong?.album_name ?: "No album",
                fontSize = 16.sp,
                modifier = Modifier
                    .offset(y = (-6).dp)
                    .padding(start = 14.dp)
            )

            ListeningWidget(currentSong)

            if (currentSong == null) return@Column

            Spacer(
                modifier = Modifier
                    .height(42.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SOTDWidget(sotd, homeViewModel)

                /**
                 * lastListened = LastListened(
                 *                     name = "Last listened song",
                 *                     album_image = "https://i.scdn.co/image/ab67616d0000b273f4b3b3b3b3b3b3b3b3b3b3b3",
                 *                     song_link = "https://open.spotify.com/track/3ZFTkvIE7kyPt6Nu3PEa7V?si=8b6e4",
                 *                     album_name = "Album name",
                 *                     author = "Author",
                 *                     explicit = false,
                 *                 )
                 */
                LastListenedWidget(viewModel = homeViewModel)
            }
        }
    }
}