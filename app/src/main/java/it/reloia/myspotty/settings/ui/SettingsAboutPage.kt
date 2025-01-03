package it.reloia.myspotty.settings.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.reloia.myspotty.OtherActivity
import it.reloia.myspotty.ui.theme.DarkRed
import me.zhanghai.compose.preference.LocalPreferenceFlow
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.preference
import me.zhanghai.compose.preference.textFieldPreference

@Composable
fun SettingsAboutPage() {
    val context = LocalContext.current

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

            item {
                Spacer(
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                Text(
                    text = "About",
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                )

                Text(
                    text = "how does it work?",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                Column(
                    Modifier.fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Create your personal mySpottyAPI instance by following the instructions at ")
                            withLink(
                                LinkAnnotation.Url(url = "https://github.com/ReLoia/mySpottyAPI")
                            ) {
                                withStyle(
                                    style = SpanStyle(
                                        color = Color.Red
                                    )
                                ) {
                                    append("this repository")
                                }
                            }
                            append(" then simply host it on your server and set the API URL and password in the settings.")
                        }
                    )

                }
            }

            preference(
                key = "about",
                title = { Text("Github Repo") },
                summary = { Text("Click here to open the Repo of this app's source code") },
                onClick = {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data = Uri.parse("https://github.com/ReLoia/mySpotty")
                    (context as OtherActivity).startActivity(browserIntent)
                },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            item {
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