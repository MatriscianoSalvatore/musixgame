package com.matrisciano.musixmatch.ui.main.leaderboard

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser

@Composable
fun LeaderboardScreen(user: FirebaseUser?, leaderboard: HashMap<String, Long>) {
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

        var sortedLeaderboard: MutableMap<String, Long> = LinkedHashMap()
        leaderboard.entries.sortedByDescending { it.value }
            .forEach { sortedLeaderboard[it.key] = it.value }

        var i = 0
        for (element in sortedLeaderboard) {
            i++
            Text(
                i.toString() + "° " + element.key + ": " + element.value + " musixpoints",
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontWeight = if (element.key == user?.email) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}