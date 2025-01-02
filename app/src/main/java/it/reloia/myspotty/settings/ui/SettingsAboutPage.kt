package it.reloia.myspotty.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.reloia.myspotty.ui.theme.DarkRed
import me.zhanghai.compose.preference.LocalPreferenceFlow
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.switchPreference
import me.zhanghai.compose.preference.textFieldPreference

@Composable
fun SettingsAboutPage() {
    ProvidePreferenceLocals (flow = LocalPreferenceFlow.current) {
        LazyColumn (
            modifier = Modifier.fillMaxSize()
                .background(
                    color = DarkRed
                ),
        ) {
            item {
                Text(
                    text = "Settings",
                    modifier = Modifier.padding(start = 16.dp),
                )
            }

            textFieldPreference(
                key = "api_url",
                title = { Text("API Url") },
                defaultValue = "",
                summary = { Text("The URL of the API server") },
                textToValue = { it }
            )

            textFieldPreference(
                key = "api_password",
                title = { Text("API Password") },
                defaultValue = "",
                summary = { Text("The password used for some API calls") },
                textToValue = { it }
            )

            switchPreference(
                key = "show_api_name",
                title = { Text("Show API name") },
                defaultValue = false,
                summary = { Text("In the main screen") },
            )

            item {
                Spacer(
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                Text(
                    text = "About",
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
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
    }
}