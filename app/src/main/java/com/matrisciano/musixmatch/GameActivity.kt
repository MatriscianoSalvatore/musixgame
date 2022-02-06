package com.matrisciano.musixmatch

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.loseRed
import com.matrisciano.musixmatch.ui.theme.musixmatchPinkLight
import com.matrisciano.musixmatch.ui.theme.winGreen

class GameActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private val maxChars = 235;
    private var testLyrics =
        "Vespe truccate anni '60\nGirano in centro sfiorando i 90\nRosse di fuoco, comincia la danza\nDi frecce con dietro attaccata una targa\nDammi una Special, l'estate che avanza\nDammi una Vespa e ti porto in vacanza\nMa quanto è bello andare in giro con le ali sotto ai piedi\nSe hai una\nVespa Special che ti toglie i problemi\nMa quanto è bello andare in giro per i colli bolognesi\nSe hai una Vespa Special che i toglie i problemi\nE la scuola non va\nMa ho una Vespa, una donna non ho\nHo una Vespa, domenica è già\nE una Vespa mi porterà (Mi porterà, mi porterà)\nFuori città\nFuori città\nFuori città\nFuori città\nFuori città\nEsco di fretta dalla mia stanza\nA marce ingranate dalla prima alla quarta\nDevo fare in fretta, devo andare a una festa\nFammi fare un giro prima sulla mia Vespa\nDammi una Special, l'estate che avanza\nDammi una Vespa e ti porto in vacanza\nMa quanto è bello andare in giro con le ali sotto ai piedi\nSe hai una Vespa Special che ti toglie i problemi\nMa quanto è bello andare in giro per i colli bolognesi\nSe hai una Vespa Special che i toglie i problemi\nE la scuola non va\nMa ho una Vespa, una donna non ho\nHo una\nVespa, domenica è già\nE una Vespa mi porterà (Mi porterà, mi porterà)\nFuori città\nFuori città\nFuori città"
    private var replacedTestLyrics = ""
    private var replacedWord = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        var startChar = 0
        if (testLyrics.length > maxChars * 3) startChar = (0..testLyrics.length/3).random()
        else if (testLyrics.length > maxChars * 2) startChar = (0..testLyrics.length/2).random()
        testLyrics = testLyrics.substring(startChar, testLyrics.length - 1)
        testLyrics = testLyrics.substring(testLyrics.indexOf(" ", startChar))
        testLyrics = testLyrics.substring(0, maxChars)
        testLyrics = testLyrics.substring(0, testLyrics.lastIndexOf(" "))
        if (startChar != 0) testLyrics = "... $testLyrics"
        testLyrics += " ..."
        var words = testLyrics.split(" ", "\n", "'", ",", ";", ".", ":", "!", "?")
        var found = false
        while (!found) {
            var randomNumber = (words.indices).random()
            replacedWord = words[randomNumber]
            if (replacedWord.length > 3) {
                found = true
                var replacement = ""
                for (char in replacedWord)
                    replacement += "*"
                replacedTestLyrics = testLyrics.replaceFirst(replacedWord, replacement)
            }
        }

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
    fun GameScreen(navCtrl: NavController, user: FirebaseUser) {
        MusixmatchPinkTheme()
        {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
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
                        text = replacedTestLyrics,
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

                            val db = Firebase.firestore
                            var points : Long
                            db.collection("users")
                                .get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        Log.d(ControlsProviderService.TAG, "${document.id} => ${document.data}")
                                        if (document.data["email"] == user?.email) {
                                            points = document.data["points"] as Long
                                            if (answer.toLowerCase().trim() == replacedWord.toLowerCase().trim())
                                                db.collection("users").document(document.id).update("points", points + 5)
                                            else db.collection("users").document(document.id).update("points", points - 1)
                                        }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w(ControlsProviderService.TAG, "Error getting documents.", exception)
                                }


                            if (answer.toLowerCase().trim() == replacedWord.toLowerCase().trim())
                                navCtrl.navigate("win_screen")
                            else navCtrl.navigate("lose_screen")

                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ),
                        enabled = (answer != "")
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
                    .fillMaxSize()
                    .background(winGreen),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Congratulations, you won 5 Musixpoints!",
                        color = Color.White,
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(30.dp)
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
                        Text(text = "OK", fontSize = 18.sp)
                    }
                }
            }
        }
    }

    @Composable
    fun LoseScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize()
                    .background(loseRed),

                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Wrong answer, you lost 1 Musixpoint!",
                        color = Color.White,
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(30.dp)
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
                        Text(text = "OK", fontSize = 18.sp)
                    };
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
                fontSize = 24.sp
            ),
            label = { Text(hint) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        )
    }

    @Composable
    fun Navigation(user: FirebaseUser?) {
        val navCtrl = rememberNavController()
        NavHost(navCtrl, "game_screen") {
            composable("game_screen") {
                GameScreen(navCtrl, user!!)
            }
            composable("win_screen") {
                WinScreen(navCtrl)
            }
            composable("lose_screen") {
                LoseScreen(navCtrl)
            }
        }
    }
}
