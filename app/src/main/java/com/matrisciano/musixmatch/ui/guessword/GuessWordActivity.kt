package com.matrisciano.musixmatch.ui.guessword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
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
import com.matrisciano.musixmatch.ui.main.MainActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.loseRed
import com.matrisciano.musixmatch.ui.theme.musixmatchPinkLight
import com.matrisciano.musixmatch.ui.theme.winGreen
import java.util.*

class GuessWordActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private val maxChars = 145
    private val safeChars = 30
    private var replacedTestLyrics = ""
    private var replacedWord = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth //TODO: create FirebaseAuth Repository
        val currentUser = auth.currentUser

        setLyricsForGame()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(14.dp)
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
                            play(user, navCtrl, answer)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ),
                        enabled = (answer != "")
                    ) {
                        Text(text = "CONFIRM", fontSize = 18.sp)
                    }
                }
            }
        }
    }

    @Composable
    fun WinScreen() {
        MusixmatchPinkTheme {
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
                            startActivity(Intent(this@GuessWordActivity, MainActivity::class.java))
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
    fun LoseScreen() {
        MusixmatchPinkTheme {
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
                            startActivity(Intent(this@GuessWordActivity, MainActivity::class.java))
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

    @Composable
    fun Navigation(user: FirebaseUser?) {
        val navCtrl = rememberNavController()
        NavHost(navCtrl, "game_screen") {
            composable("game_screen") {
                GameScreen(navCtrl, user!!)
            }
            composable("win_screen") {
                WinScreen()
            }
            composable("lose_screen") {
                LoseScreen()
            }
        }
    }

    private fun setLyricsForGame() {
        var lyrics = intent.getStringExtra("lyrics")
        lyrics = lyrics!!.replace("******* This Lyrics is NOT for Commercial use *******", "")
        lyrics = lyrics.replace("\\n", "\n")
        lyrics = lyrics.replace("\\\"", "\"")
        //lyrics = lyrics!!.toByteArray(Charsets.UTF_8).toString(Charsets.UTF_8)

        var startChar = 0
        when {
            (lyrics.length > maxChars * 4) -> startChar =
                (0 until (lyrics.length / 4 * 3 - safeChars)).random()
            (lyrics.length > maxChars * 3) -> startChar =
                (0 until (lyrics.length / 3 * 2 - safeChars)).random()
            (lyrics.length > maxChars * 2) -> startChar =
                (0 until (lyrics.length / 2 - safeChars)).random()
            (lyrics.length > 2 * safeChars) -> startChar = (0 until safeChars).random()
        }
        lyrics = lyrics.substring(startChar)

        val minSpaceChar = lyrics.indexOf(" ")
        val minNewLineChar = lyrics.indexOf("\n")
        lyrics = lyrics.substring(minSpaceChar.coerceAtMost(minNewLineChar))

        if (lyrics.length > maxChars) lyrics = lyrics.substring(0, maxChars)

        val maxSpaceChar = lyrics.lastIndexOf(" ")
        val maxNewLineChar = lyrics.lastIndexOf("\n")
        lyrics = lyrics.substring(0, maxSpaceChar.coerceAtLeast(maxNewLineChar))

        if (startChar != 0) lyrics = "... $lyrics"
        lyrics += " ..."
        val words = lyrics.split(" ", "\n", "'", ",", ";", ".", ":", "!", "?")
        var found = false
        while (!found) {
            val randomNumber = (words.indices).random()
            replacedWord = words[randomNumber]
            if (replacedWord.length > 3) {
                found = true
                var replacement = ""
                for (char in replacedWord)
                    replacement += "*"
                replacedTestLyrics = lyrics.replaceFirst(replacedWord, replacement)
            }
        }
    }

    private fun play(user: FirebaseUser, navCtrl: NavController, answer: String) {
        val db = Firebase.firestore //TODO: use Users Repository
        var points: Long
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(
                        "Firestore",
                        "${document.id} => ${document.data}"
                    )
                    if (document.data["email"] == user.email) {
                        points = document.data["points"] as Long
                        if (answer.lowercase(Locale.getDefault())
                                .trim() == replacedWord.lowercase(Locale.getDefault())
                                .trim()
                        ) {
                            db.collection("users").document(document.id)
                                .update("points", points + 5)
                            navCtrl.navigate("win_screen") {
                                popUpTo("game_screen") {
                                    inclusive = true
                                }
                            }
                        } else {
                            db.collection("users").document(document.id)
                                .update("points", points - 1)
                            navCtrl.navigate("lose_screen") {
                                popUpTo("game_screen") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext, "Database error",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}