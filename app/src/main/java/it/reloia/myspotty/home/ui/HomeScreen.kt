package it.reloia.myspotty.home.ui

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

            AutoScrollingMarqueeText(
                currentSong?.name ?: "No song playing",
                modifier = Modifier
                    .padding(horizontal = 14.dp),
                fontSize = 24.sp,
                color = Color.White
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
                    .height(6.dp)
            )

            Column(
                modifier = Modifier
                    .padding(top = 38.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "songs of the day",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                )

                // TODO: move to a separate composable and make the values dynamic
                LazyRow(
                    Modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth()
                ) {
                    items(sotd.size) {
                        // TODO: move to a different composable
                        // TODO: make a bottom sheet with the song details and buttons to remove from SOTD
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(100.dp)
                                .clickable { println("Clicked on SOTD song: ${sotd[it]}") },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF2E2A2A)
                            )
                        ) {
                            AsyncImage(
                                model = sotd[it].album,
                                contentDescription = "Album image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }

                // TODO: make the value dynamic and not hardcoded, also make it a separate composable, also hide it if the song on top is not playing (aka. currentSong == lastSong)
                Text(
                    "last listened song",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                )

//            LazyRow (
//                Modifier
//                    .padding(bottom = 24.dp)
//                    .fillMaxWidth()
//            ) {
//                items(10) {
//                    Card(
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .size(100.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = Color(0xFF2E2A2A)
//                        )
//                    ) {
//                        Text("Song")
//                    }
//                }
//            }
            }
        }
    }
}