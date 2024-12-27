package it.reloia.myspotty.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToggleSetting(
    title: String,
    description: String? = null,
) {
    // TODO: add the setting key as a parameter and a way to save the setting
    // TODO: load the setting value from the ViewModel
    var checked by remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(
                title,
                fontSize = 20.sp
            )
            if (description != null) {
                Text(
                    description,
                    fontSize = 14.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = { checked = it }
        )
    }
}