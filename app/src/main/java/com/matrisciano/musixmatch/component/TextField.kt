package com.matrisciano.musixmatch.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.matrisciano.musixmatch.ui.theme.musixmatchPinkLight

@Composable
fun MusixGameTextField(
    value: String,
    onInputChanged: (String) -> Unit,
    hint: String,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        maxLines = 1,
        singleLine = true,
        onValueChange = onInputChanged,
        modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 0.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = musixmatchPinkLight,
            textColor = Color(0xFFFFFFFF),
            unfocusedLabelColor = Color(0x70FFFFFF),
        ),
        label = { Text(hint) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}