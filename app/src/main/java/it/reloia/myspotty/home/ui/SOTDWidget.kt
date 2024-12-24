package it.reloia.myspotty.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import it.reloia.myspotty.home.domain.model.SOTD

@Composable
fun SOTDWidget(sotd: List<SOTD>, viewModel: HomeViewModel) {
    Text(
        "songs of the day",
        fontSize = 18.sp,
        modifier = Modifier
            .padding(bottom = 6.dp)
    )

    LazyRow(
        Modifier
            .padding(bottom = 24.dp)
            .fillMaxWidth()
    ) {
        items(sotd.size) {
            // TODO: make a bottom sheet with the song details and buttons to remove from SOTD
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(96.dp)
                    .clickable { println("Clicked on SOTD song: ${sotd[it]}") },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2E2A2A)
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(sotd[it].album)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .networkCachePolicy(CachePolicy.READ_ONLY)
                            .build(),
                        contentDescription = sotd[it].name + " album cover",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Text(
                        sotd[it].name,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(Color(0x60000000))
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .basicMarquee(
                                iterations = Int.MAX_VALUE
                            ),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}