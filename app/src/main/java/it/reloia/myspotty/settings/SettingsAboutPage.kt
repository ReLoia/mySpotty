package it.reloia.myspotty.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.reloia.myspotty.settings.components.TextSetting
import it.reloia.myspotty.settings.components.ToggleSetting
import it.reloia.myspotty.ui.theme.DarkRed

@Composable
fun SettingsAboutPage() {
    Column (
        modifier = Modifier
            .background(
                color = DarkRed
            )
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Settings",
            modifier = Modifier.padding(bottom = 16.dp),
        )
        TextSetting(
            title = "API Url",
            description = "The URL of the API server",

        )
        TextSetting(
            title = "API Password",
            description = "The password used for some API calls",
            shouldHide = true
        )
        ToggleSetting(
            title = "Show API name",
            description = "In the main screen",
        )

        Spacer(
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Text(
            text = "About",
            modifier = Modifier.padding(bottom = 16.dp),
        )
        Text(
            text = "mySpotty",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "manage your personal spotify api",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = "by reloia",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}