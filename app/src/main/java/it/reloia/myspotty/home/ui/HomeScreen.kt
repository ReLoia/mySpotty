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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.reloia.myspotty.R

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
        isRefreshing = homeViewModel.isRefreshing.value
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Text(
                (currentSong?.author ?: stringResource(R.string.song_no_author)),
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
                (currentSong?.name ?: stringResource(R.string.song_not_playing)),
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
                (currentSong?.album_name ?: stringResource(R.string.song_no_album)),
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

                if (lastListened != null) LastListenedWidget(lastListened, viewModel = homeViewModel)
            }
        }
    }
}