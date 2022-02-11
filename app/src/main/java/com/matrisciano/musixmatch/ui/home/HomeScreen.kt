package com.matrisciano.musixmatch.ui.home

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.matrisciano.musixmatch.component.MusixGameTextField
import com.matrisciano.musixmatch.ui.GuessWordActivity
import com.matrisciano.musixmatch.ui.MainActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(activity: MainActivity) {
    val viewModel = getViewModel<HomeViewModel>()
    MusixmatchTheme() {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column(
            ) {

                Column( //TODO: use column item instead of Columns inside Column
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Game 1: guess the missing word",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
                    )

                    var title by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        title,
                        onInputChanged = { title = it },
                        hint = "Title",
                    )

                    var artist by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        artist,
                        onInputChanged = { artist = it },
                        hint = "Artist",
                    )

                    val scope = rememberCoroutineScope()

                    TextButton(
                        modifier = Modifier
                            .padding(15.dp)
                            .width(215.dp),
                        onClick = {
                            scope.launch {
                                when (val result = viewModel.getTrackID(artist, title)) {
                                    is Result.Success -> {
                                        val trackID =
                                            result.value.message?.body?.track_list?.get(0)?.track?.track_id
                                        Log.d("HomeScreen", "TrackID: $trackID")

                                        scope.launch {
                                            when (val result =
                                                viewModel.getLyrics(trackID.toString())) {
                                                is Result.Success -> {
                                                    val lyrics =
                                                        result.value.message?.body?.lyrics?.lyrics_body
                                                    Log.d("HomeScreen", "Lyrics: $lyrics")

                                                    val intent = Intent(
                                                        activity,
                                                        GuessWordActivity::class.java
                                                    )
                                                    intent.putExtra("lyrics", lyrics)
                                                    startActivity(activity, intent, null)

                                                }
                                                is Result.Error -> {
                                                    Log.d(
                                                        "HomeScreen",
                                                        "Lyrics error: ${result.message}"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    is Result.Error -> {
                                        Log.d("HomeScreen", "TrackID error: ${result.message}")
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = (title != "" && artist != "")
                    ) {
                        Text(text = "GUESS THE WORD! \uD83C\uDFA4")
                    }

                    Column(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Game 2: guess the singer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(0.dp, 90.dp, 0.dp, 16.dp),
                        )

                        TextButton(
                            modifier = Modifier
                                .width(215.dp),
                            onClick = {
                                scope.launch {
                                    when (val result = viewModel.getTopTracks()) {
                                        is Result.Success -> {
                                            val topTracks =
                                                result.value.message?.body?.track_list
                                            Log.d("HomeScreen", "TopTracks: $topTracks")
                                        }
                                        is Result.Error -> {
                                            Log.d("HomeScreen", "TopTracks error: ${result.message}")
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ), enabled = true
                        ) {
                            Text(text = "WHO SINGS? \uD83C\uDFA4")
                        }
                    }
                }
            }
        }
    }
}