package com.matrisciano.musixmatch.ui.main.home

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.component.MusixGameTextField
import com.matrisciano.musixmatch.ui.guessword.GuessWordActivity
import com.matrisciano.musixmatch.ui.main.MainActivity
import com.matrisciano.musixmatch.ui.whosings.WhoSingsActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.network.model.TopTrack
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private const val maxTracks = 60 //divisible for matchesNumber
private const val matchesNumber = 3
private var correctIndexes = Array<Int?>(matchesNumber) { null }
private var artists = Array(matchesNumber) { arrayOf("", "", "") }
private var tracks = Array<String?>(matchesNumber) { null }
private var topTracks: List<TopTrack> = emptyList()


@Composable
fun HomeScreen(activity: MainActivity) {
    val viewModel = getViewModel<HomeViewModel>()
    MusixmatchTheme {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column {

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
                        hint = stringResource(R.string.title_hint),
                    )

                    var artist by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        artist,
                        onInputChanged = { artist = it },
                        hint = stringResource(R.string.artist_hint),
                    )

                    val scope = rememberCoroutineScope()

                    TextButton(
                        modifier = Modifier
                            .padding(15.dp)
                            .width(215.dp),
                        onClick = {
                            startGuessWordGame(scope, viewModel, artist, title, activity)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = (title != "" && artist != "")
                    ) {
                        Text(text = stringResource(R.string.guess_the_word_button))
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
                                startWhoSingsGame(scope, viewModel, activity)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ), enabled = true
                        ) {
                            Text(text = stringResource(R.string.who_sings_button))
                        }
                    }
                }
            }
        }
    }
}

private fun showTrackNotFoundToast(baseContext: Activity) {
    Toast.makeText(
        baseContext, "Track not found",
        Toast.LENGTH_SHORT
    ).show()
}

private fun startGuessWordGame(
    scope: CoroutineScope,
    viewModel: HomeViewModel,
    artist: String,
    title: String,
    activity: Activity
) {
    scope.launch {
        try {
            when (val result = viewModel.getTrackID(artist, title)) {
                is Result.Success -> {
                    val trackID =
                        result.value.message?.body?.track_list?.get(0)?.track?.track_id
                    Log.d("HomeScreen", "TrackID: $trackID")

                    scope.launch {
                        when (val res =
                            viewModel.getLyrics(trackID.toString())) {
                            is Result.Success -> {
                                val lyrics =
                                    res.value.message?.body?.lyrics?.lyrics_body
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
                                    "Lyrics error: ${res.message}"
                                )
                                showTrackNotFoundToast(activity)
                            }
                        }
                    }
                }
                is Result.Error -> {
                    Log.d("HomeScreen", "TrackID error: ${result.message}")
                    showTrackNotFoundToast(activity)
                }
            }
        } catch (e: Exception) {
            showTrackNotFoundToast(activity)
        }
    }
}

private fun startWhoSingsGame(scope: CoroutineScope, viewModel: HomeViewModel, activity: Activity) {
    try {
        scope.launch {

            correctIndexes = Array(matchesNumber) { null }
            artists = Array(matchesNumber) { arrayOf("", "", "") }
            tracks = Array(matchesNumber) { null }

            if (topTracks.isEmpty()) {

                when (val result = viewModel.getTopTracks()) {
                    is Result.Success -> {
                        topTracks =
                            result.value.message?.body?.track_list!!
                        Log.d("HomeScreen", "TopTracks: $topTracks")

                        initWhoSingsGame(scope, viewModel, activity)
                    }

                    is Result.Error -> {
                        Log.d(
                            "HomeScreen",
                            "TopTracks error: ${result.message}"
                        )
                    }
                }
            } else initWhoSingsGame(scope, viewModel, activity)
        }
    } catch (e: Exception) {
        showTrackNotFoundToast(activity)
    }
}

private fun initWhoSingsGame(scope: CoroutineScope, viewModel: HomeViewModel, activity: Activity) {
    val indexes = Array(matchesNumber) { arrayOf(0, 1, 2) }
    for (i in 0 until matchesNumber) {

        indexes[i][0] =
            ((maxTracks / matchesNumber * i) until ((maxTracks / matchesNumber * (i + 1)))).random()
        while (artists[i][0] == artists[i][1] || artists[i][1] == artists[i][2] || artists[i][0] == artists[i][2]) {

            indexes[i][1] = (0 until maxTracks).random()
            indexes[i][2] = (0 until maxTracks).random()

            tracks[i] =
                Gson().newBuilder().disableHtmlEscaping()
                    .create()
                    .toJson(
                        topTracks[indexes[i][0]].track?.track_id
                    )

            for (j in 0..2)
                artists[i][j] =
                    Gson().newBuilder()
                        .disableHtmlEscaping().create()
                        .toJson(
                            topTracks[indexes[i][j]].track?.artist_name
                        )
        }

        val correctArtist = artists[i][0]
        artists[i].shuffle()
        correctIndexes[i] =
            artists[i].indexOf(correctArtist)
    }


    scope.launch {
        when (val result =
            viewModel.getSnippet(tracks[0]!!)) {
            is Result.Success -> {
                val snippet =
                    result.value.message?.body?.snippet?.snippet_body
                Log.d("HomeScreen", "Snippet: $snippet")

                if (snippet != null) {
                    val intent = Intent(
                        activity,
                        WhoSingsActivity::class.java
                    )
                    for (i in 0 until matchesNumber) {
                        for (j in 0..2) intent.putExtra(
                            "artist" + i + "_" + j,
                            artists[i][j]
                        )
                        intent.putExtra(
                            "correctIndex$i",
                            correctIndexes[i]
                        )
                        intent.putExtra(
                            "track$i",
                            tracks[i]
                        )
                        intent.putExtra("snippet", snippet)
                    }
                    startActivity(activity, intent, null)
                }
            }
            is Result.Error -> {
                Log.d(
                    "HomeScreen",
                    "Snippet error: ${result.message}"
                )
                showTrackNotFoundToast(activity)
            }
        }
    }
}