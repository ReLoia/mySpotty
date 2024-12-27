package it.reloia.myspotty.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import it.reloia.myspotty.OtherActivity
import it.reloia.myspotty.ui.theme.DarkRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    MediumTopAppBar(
//        title = { Text("Settings and About") },
        title = { Text("mySpotty") },
        navigationIcon = {
            IconButton(onClick = {
                (context as OtherActivity).finish()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = DarkRed, scrolledContainerColor = DarkRed)
    )
}