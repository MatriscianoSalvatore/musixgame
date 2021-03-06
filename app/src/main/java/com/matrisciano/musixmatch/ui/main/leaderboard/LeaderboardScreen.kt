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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.musixmatch.utils.Preferences
import com.matrisciano.network.model.User
import com.matrisciano.network.utils.Result
import org.koin.androidx.compose.getViewModel

@Composable
fun LeaderboardScreen() {
    var leaderboard: List<User> = emptyList()
    val viewModel = getViewModel<LeaderboardViewModel>()
    var email = ""
    val userID = Preferences.defaultPref(LocalContext.current).getString("userID", null)
    Log.d("LeaderboardScreen", "User: $userID")

    if (userID != null) {
        viewModel.getUser(userID).observeAsState().value.let { user ->
            if (user is Result.Success) {
                Log.d("LeaderboardScreen", "User: ${user.value}")
                email = user.value.email!!
            }
        }
    }

    viewModel.getAllUsers().observeAsState().value.let { users ->
        if (users is Result.Success) {
            Log.d("LeaderboardScreen", "Users: ${users.value}")
            leaderboard = users.value.sortedByDescending { it.points }
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
            var positionIndex = 0
            leaderboard.forEach {
                positionIndex++
                Text(
                    text = positionIndex.toString() + "?? " + it.email + ": " + it.points + " " + stringResource(
                        R.string.musixpoints
                    ),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = if (it.email == email) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}