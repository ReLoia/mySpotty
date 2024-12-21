package it.reloia.myspotty.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.reloia.myspotty.home.ui.domain.model.CurrentSong

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(
            modifier = Modifier
                .height(36.dp)
        )

        ListeningWidget(
            currentSong = CurrentSong(
                author = "",
                name = "",
                songLink = "",
                duration = 0,
                progress = 0,
                explicit = true,
                playing = true,
                albumName = "",
                albumImage = ""
            )
        )

        Spacer(
            modifier = Modifier
                .height(58.dp)
        )

        Text(
            "Song name",
            fontSize = 28.sp
        )
        Text(
            "Album name",
            fontSize = 17.sp,
            modifier = Modifier
                .offset(y = (-6).dp)
        )
    }
}