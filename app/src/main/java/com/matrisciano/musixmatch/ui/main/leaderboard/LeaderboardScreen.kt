package com.matrisciano.musixmatch.ui.main.leaderboard

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.network.model.User
import com.matrisciano.network.utils.Result
import org.koin.androidx.compose.getViewModel

var leaderboard: List<User> = emptyList()

@Composable
fun LeaderboardScreen(user: FirebaseUser?) {

    val viewModel = getViewModel<LeaderboardViewModel>()
    Log.d("LeaderboardScreen", "User: ${user?.uid}")

    viewModel.getAllUsers().observeAsState().value.let {
        if (it is Result.Success) {
            leaderboard = it.value
            Log.d("LeaderboardScreen", "Users: ${it.value}")
        }
    }

    MusixmatchTheme {
        val scrollState = rememberScrollState()
        Column( //TODO: use lazy column
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(6.dp, 58.dp, 0.dp, 0.dp)
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical
                )
        ) {
            val sortedLeaderboard = leaderboard.sortedByDescending { it.points }
            var positionIndex = 0
            sortedLeaderboard.forEach {
                positionIndex++
                Text(
                    text = positionIndex.toString() + "° " + it.email + ": " + it.points + " " + stringResource(
                        R.string.musixpoints
                    ),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = if (it.email == user?.email) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}