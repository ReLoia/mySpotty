package it.reloia.myspotty.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.reloia.myspotty.home.ui.domain.model.CurrentSong


@Composable
fun ListeningWidget(currentSong: CurrentSong) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(14.dp)
        )

        Box(
            modifier = Modifier
                .width(340.dp)
                .height(340.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2E2A2A)),
        ) {
            IconButton(
                onClick = { /* do something */ },
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
                    tint = Color.White,
                    modifier = Modifier.size(19.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(24.dp)
        )

        val clampedProgress = .4f.coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .height(8.dp)
                .width(290.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF2E2A2A))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(clampedProgress)
                    .background(Color.White)
            )
        }
    }
}