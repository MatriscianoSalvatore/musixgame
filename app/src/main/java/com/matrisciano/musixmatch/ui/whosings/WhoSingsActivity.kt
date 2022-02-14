package com.matrisciano.musixmatch.ui.whosings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.google.firebase.auth.FirebaseUser
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.ui.main.MainActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.loseRed
import com.matrisciano.musixmatch.ui.theme.winGreen
import com.matrisciano.musixmatch.utils.Preferences
import com.matrisciano.musixmatch.utils.Preferences.get
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

class WhoSingsActivity : ComponentActivity() {
    private var snippet: String? = ""
    private val maxArtistChars = 55
    private val matchesNumber = 3
    private var tracks = Array<String?>(matchesNumber) { null }
    private var correctIndexes = Array<Int?>(matchesNumber) { null }
    private var artists = Array(matchesNumber) { arrayOf("", "", "") }
    private var currentMatch = 0
    private val maxTimer: Long = 10000
    private val firebaseUser: FirebaseUser = Preferences.defaultPref(baseContext)["user", null] as FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityParams()

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
            Navigation()
        }
    }

    @Composable
    fun GameScreen(navCtrl: NavController) {
        MusixmatchPinkTheme()
        {
            val viewModel = getViewModel<WhoSingsViewModel>()

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
                        text = "\"" + snippet!! + "\"",
                        color = Color.White,
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 25.dp)
                    )

                    for (i in 0..2) {

                        TextButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(400.dp)
                                .padding(28.dp),
                            onClick = {
                                play((correctIndexes[currentMatch] == i), firebaseUser, viewModel, navCtrl)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ),
                            enabled = true
                        ) {
                            if (artists[currentMatch][i].length > maxArtistChars) {
                                artists[currentMatch][i] =
                                    artists[currentMatch][i].substring(0, maxArtistChars)
                                artists[currentMatch][i] = "$artists[i]..."
                            }
                            Text(
                                text = artists[currentMatch][i].replace("\"", ""),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Timer(navCtrl, firebaseUser, viewModel)
                }
            }
        }
    }

    @Composable
    fun WinScreen(navCtrl: NavController) {
        val viewModel = getViewModel<WhoSingsViewModel>()
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

                    val scope = rememberCoroutineScope()
                    TextButton(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(28.dp),

                        onClick = {
                            scope.launch { nextMatch(viewModel, navCtrl) }
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
    fun LoseScreen(navCtrl: NavController) {
        val viewModel = getViewModel<WhoSingsViewModel>()
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

                    val scope = rememberCoroutineScope()

                    TextButton(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(28.dp),
                        onClick = {
                            scope.launch { nextMatch(viewModel, navCtrl) }
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
    fun Navigation() {
        val navCtrl = rememberNavController()
        NavHost(navCtrl, "game_screen") {
            composable("game_screen") {
                GameScreen(navCtrl)
            }
            composable("win_screen") {
                WinScreen(navCtrl)
            }
            composable("lose_screen") {
                LoseScreen(navCtrl)
            }
        }
    }

    private suspend fun nextMatch(
        viewModel: WhoSingsViewModel?,
        navCtrl: NavController
    ) {
        currentMatch++
        if (currentMatch < matchesNumber) {

            when (val result = viewModel?.getSnippet(tracks[currentMatch]!!)) {
                is Result.Success -> {
                    val currentSnippet =
                        result.value.message?.body?.snippet?.snippet_body
                    Log.d("WhoSingActivity", "Snippet: $currentSnippet")
                    snippet = currentSnippet
                    navCtrl.navigate("game_screen") {
                        popUpTo("game_screen") {
                            inclusive = true
                        }
                    }
                }

                is Result.Error -> {
                    Log.d("WhoSingActivity", "Snipptet error: ${result.message}")
                    showTrackNotFoundToast()
                }
            }

        } else startActivity(
            Intent(
                this@WhoSingsActivity,
                MainActivity::class.java
            )
        )
    }

    private fun showTrackNotFoundToast() {
        Toast.makeText(
            baseContext, "Track not found",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun play(
        win: Boolean,
        user: FirebaseUser,
        viewModel: WhoSingsViewModel,
        navCtrl: NavController
    ) {
        if (win) {
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

    @Composable
    private fun Timer(navCtrl: NavController, user: FirebaseUser, viewModel: WhoSingsViewModel) {
        val timeLeftMs by rememberCountdownTimerState(navCtrl, user, viewModel)
        Text(
            text = (timeLeftMs / 1000).toString(),
            color = Color.White,
            fontSize = 35.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(30.dp)
        )
    }

    @Composable
    private fun rememberCountdownTimerState(
        navCtrl: NavController,
        user: FirebaseUser,
        viewModel: WhoSingsViewModel,
    ): MutableState<Long> {
        val timeLeft = remember { mutableStateOf(maxTimer) }
        LaunchedEffect(maxTimer, 1000) {
            while (timeLeft.value > 1) {
                delay(1000)
                timeLeft.value = (timeLeft.value - 1000).coerceAtLeast(0)
            }
            play(false, user, viewModel, navCtrl)
            navCtrl.navigate("lose_screen") {
                popUpTo("game_screen") {
                    inclusive = true
                }
            }
        }
        return timeLeft
    }

    private fun getActivityParams() {
        for (i in 0 until matchesNumber) {
            for (j in 0..2) artists[i][j] = intent.getStringExtra("artist" + i + "_" + j)!!
            tracks[i] = intent.getStringExtra("track$i")
            correctIndexes[i] = intent.getIntExtra("correctIndex$i", 0)
        }
        snippet = intent.getStringExtra("snippet")
    }
}