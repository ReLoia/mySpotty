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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import it.reloia.myspotty.R
import it.reloia.myspotty.core.domain.model.SOTD
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun SOTDWidget(sotd: List<SOTD>, viewModel: HomeViewModel) {
    Text(
        stringResource(R.string.songs_of_the_day),
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
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .size(92.dp)
                    .clickable { viewModel.toggleSOTDSheet(sotd[it]) },
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
                        contentDescription = stringResource(
                            R.string.song_album_cover,
                            sotd[it].name
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )

                    val date = Instant.ofEpochMilli(sotd[it].date).atZone(ZoneId.systemDefault()).toLocalDate()

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
                            color = if (date == LocalDate.now())
                                Color(0xFFFFBF00)
                            else Color.White,
                                fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}