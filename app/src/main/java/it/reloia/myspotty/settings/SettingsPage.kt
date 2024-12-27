package it.reloia.myspotty.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingElement(content: @Composable () -> Unit) {
    Row (
        modifier = Modifier
            .padding(12.dp)
    ) {
        content()
    }
}

@Composable
fun SettingsPage() {
    Column {
        SettingElement {
            Text("API Url")
        }
    }
}