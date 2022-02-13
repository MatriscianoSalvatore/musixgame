package com.matrisciano.musixmatch.ui.guessword

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.component.GameTextField
import com.matrisciano.musixmatch.ui.main.MainActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.loseRed
import com.matrisciano.musixmatch.ui.theme.winGreen
import org.koin.androidx.compose.getViewModel
import java.util.*

class GuessWordActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private val maxChars = 145
    private val safeChars = 30
    private var replacedTestLyrics = ""
    private var replacedWord = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth //TODO: use FirebaseAuth Repository
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
            val viewModel = getViewModel<GuessWordViewModel>()
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
                        hint = stringResource(R.string.missing_word_hint)
                    )

                    TextButton(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(28.dp),
                        onClick = {
                            play(user, navCtrl, answer, viewModel)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ),
                        enabled = (answer != "")
                    ) {
                        Text(text = stringResource(R.string.confirm_button), fontSize = 18.sp)
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
                        text = stringResource(R.string.win_text),
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
                        Text(text = stringResource(R.string.ok), fontSize = 18.sp)
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
                        text = stringResource(R.string.lose_text),
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
                        Text(text = stringResource(R.string.ok), fontSize = 18.sp)
                    }
                }
            }
        }
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

    private fun play(
        user: FirebaseUser,
        navCtrl: NavController,
        answer: String,
        viewModel: GuessWordViewModel
    ) {
        if (answer.lowercase(Locale.getDefault())
                .trim() == replacedWord.lowercase(Locale.getDefault())
                .trim()
        ) {
            viewModel.addPoints(user.uid, 5).observeForever {
                navCtrl.navigate("win_screen") {
                    popUpTo("game_screen") {
                        inclusive = true
                    }
                }
            }
        } else {
            viewModel.addPoints(user.uid, -1).observeForever {
                navCtrl.navigate("lose_screen") {
                    popUpTo("game_screen") {
                        inclusive = true
                    }
                }
            }
        }
    }
}