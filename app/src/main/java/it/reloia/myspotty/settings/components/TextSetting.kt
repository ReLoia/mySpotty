package it.reloia.myspotty.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.reloia.myspotty.R

@Composable
fun TextSetting(
    title: String,
    description: String? = null,
    shouldHide: Boolean? = false,
//    onClick: () -> Unit
) {
    // TODO: add the setting key as a parameter and a way to save the setting
    // maybe using a ViewModel

    var editing by remember { mutableStateOf(false) }
    // TODO: load the setting value from the ViewModel
    var text by remember { mutableStateOf("") }

    var textVisible by remember { mutableStateOf(false) }

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

        TextButton(
            onClick = {
                if (editing) {
                    // TODO: save the setting value to the ViewModel
                }
                editing = !editing
            },
        ) {
            Text(
                if (editing) "Save" else "Edit",
                fontSize = 16.sp
            )
        }
    }
    if (editing)
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (shouldHide == true && textVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                if (shouldHide == true) {
                    IconButton(
                        onClick = { textVisible = !textVisible }
                    ) {
                        Icon(
                            painter = if (textVisible) painterResource(R.drawable.baseline_visibility_off_24) else painterResource(R.drawable.baseline_visibility_24),
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = if (shouldHide == true) KeyboardType.Password else KeyboardType.Text)
        )
    HorizontalDivider()
}