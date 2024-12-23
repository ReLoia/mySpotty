package it.reloia.myspotty.home.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoScrollingMarqueeText(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = 16.sp, color: Color = Color.Black) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch  {
            while (true) {
                scrollState.animateScrollTo(
                    scrollState.maxValue,
                    animationSpec = tween(1000)
                )
                delay(2000)
                scrollState.scrollTo(0)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
    ) {
        BasicText(
            text = text,
            overflow = TextOverflow.Clip,
            style = TextStyle(
                fontSize = fontSize,
                color = color
            )
        )
    }
}
