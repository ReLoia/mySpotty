package it.reloia.myspotty.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.reloia.myspotty.home.ui.domain.model.CurrentSong

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    // TODO: make this value dynamic and not hardcoded
    val currentSong = CurrentSong(
        author = "Author",
        name = "Song name",
        songLink = "",
        duration = 1000,
        progress = 410,
        explicit = true,
        playing = true,
        albumName = "Album name",
        albumImage = ""
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {
        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        ListeningWidget(currentSong)

        Spacer(
            modifier = Modifier
                .height(28.dp)
        )

        // TODO: make these values dynamic and not hardcoded
        Text(
            currentSong.name,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(start = 14.dp)
        )
        Text(
            currentSong.albumName,
            fontSize = 17.sp,
            modifier = Modifier
                .offset(y = (-6).dp)
                .padding(start = 14.dp)
        )

        Column (
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
            LazyRow (
                Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth()
            ) {
                items(10) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(100.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E2A2A)
                        )
                    ) {
                        Text("Song")
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