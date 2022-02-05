package com.matrisciano.musixmatch

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.musixmatchPinkLight

class GameActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth


    private var testLyrics: String =
        "Vespe truccate anni '60\nGirano in centro sfiorando i 90\nRosse di fuoco, comincia la danza\nDi frecce con dietro attaccata una targa\nDammi una Special, l'estate che avanza\nDammi una Vespa e ti porto in vacanza"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        testLyrics += "..."

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            MusixmatchPinkTheme()
            {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {}
            }
            Navigation(currentUser)
        }
    }

    @Composable
    fun GameScreen(navCtrl: NavController) {
        MusixmatchPinkTheme()
        {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
                    //.padding(0.dp, 30.dp, 0.dp, 0.dp)
                contentAlignment = Alignment.Center

            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .scrollable(
                            state = scrollState,
                            orientation = Orientation.Vertical
                        ),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(
                        text = testLyrics,
                        color = Color.White,
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(15.dp)
                    )

                    var answer by rememberSaveable { mutableStateOf("") }
                    GameTextField(
                        answer,
                        onInputChanged = { answer = it },
                        hint = "Missing word",
                    )

                    TextButton(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(28.dp),
                        onClick = {
                            startActivity(Intent(this@GameActivity, MainActivity::class.java))
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = true
                    ) {
                        Text(text = "CONFIRM", fontSize = 18.sp)
                    };

                }
            }
        }
    }

    @Composable
    fun WinScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {


            }
        }
    }

    @Composable
    fun LoseScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                }
            }
        }
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
                fontSize = 24.sp),
            label = { Text(hint) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
    }

    @Composable
    fun Navigation(user: FirebaseUser?) {
        val navCtrl = rememberNavController()
        NavHost(navController = navCtrl, startDestination = "game_screen") {
            composable("game_screen") {
                GameScreen(navCtrl = navCtrl)
            }
            composable("win_screen") {
                WinScreen(navCtrl = navCtrl)
            }
            composable("lose_screen") {
                LoseScreen(navCtrl = navCtrl)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GamePreview() {
        GameScreen(rememberNavController())
    }
}
