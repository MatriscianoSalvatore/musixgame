package com.matrisciano.musixmatch.ui.home

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
import com.matrisciano.musixmatch.component.MusixGameTextField
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen() {
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
                                        Log.d("HomeScreen", "TrackID: ${result.value.message?.body?.track_list?.get(0)?.track?.track_id}")
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
                                //getTopTracks()
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