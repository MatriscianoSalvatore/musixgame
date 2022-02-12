package com.matrisciano.musixmatch.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matrisciano.musixmatch.ui.signin.SigninActivity
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

@Composable
fun SigninTextField(
    value: String,
    onInputChanged: (String) -> Unit,
    hint: String,
    textfieldType: SigninActivity.TextfieldType
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
            textColor = Color(0xFFFFFFFF),
            unfocusedLabelColor = Color(0x70FFFFFF),
        ),
        label = { Text(hint) },
        visualTransformation = if (textfieldType == SigninActivity.TextfieldType.PASSWORD) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardActions = if (textfieldType == SigninActivity.TextfieldType.PASSWORD) KeyboardActions(onDone = { focusManager.clearFocus() })
        else KeyboardActions(onDone = { focusManager.moveFocus(FocusDirection.Next) }),
        keyboardOptions = when (textfieldType) {
            SigninActivity.TextfieldType.PASSWORD -> KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
            SigninActivity.TextfieldType.EMAIL -> KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            else -> KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        },
    )
}

@Composable
fun GameTextField(
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
            .padding(18.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = musixmatchPinkLight,
            textColor = Color(0xFFFFFFFF),
            unfocusedLabelColor = Color(0x70FFFFFF),
        ),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 21.sp
        ),
        label = { Text(hint) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
    )
}