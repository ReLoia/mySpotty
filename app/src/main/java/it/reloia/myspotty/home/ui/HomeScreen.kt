package it.reloia.myspotty.home.ui

import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(paddingValues: PaddingValues, homeViewModel: HomeViewModel) {
    val currentSong by homeViewModel.currentSong.collectAsState()
    val sotd by homeViewModel.sotd.collectAsState()
    val lastListened by homeViewModel.lastListened.collectAsState()

    PullToRefreshBox (
        onRefresh = {
            homeViewModel.refresh()
        },
        isRefreshing = homeViewModel.isRefreshing
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Text(
                (currentSong?.author ?: "No author"),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        spacing = MarqueeSpacing(80.dp)
                    ),
                textAlign = TextAlign.Center
            )
            Text(
                (currentSong?.name ?: "No song playing"),
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        spacing = MarqueeSpacing(80.dp)
                    ),
                textAlign = TextAlign.Center
            )
            Text(
                (currentSong?.album_name ?: "No album"),
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

            ListeningWidget(currentSong, homeViewModel)

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
                if (lastListened != null) LastListenedWidget(lastListened, viewModel = homeViewModel)
            }
        }
    }
}