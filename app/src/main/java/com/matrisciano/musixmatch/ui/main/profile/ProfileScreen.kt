package com.matrisciano.musixmatch.ui.main.profile

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.ui.signin.SigninActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.network.model.User
import com.matrisciano.network.utils.Result
import org.koin.androidx.compose.getViewModel

private var userObj: User = User("", 0)

@Composable
fun ProfileScreen(user: FirebaseUser?, activity: Activity) {

    val viewModel = getViewModel<ProfileViewModel>()
    Log.d("LeaderboardScreen", "User: ${user?.uid}")

    if (user != null) {
        viewModel.getUser(user.uid).observeAsState().value.let {
            if (it is Result.Success) {
                userObj = it.value
                Log.d("ProfileScreen", "Users: ${it.value}")
            }
        }
    }

    MusixmatchTheme {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = stringResource(R.string.name_label) + " " + user?.displayName,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = stringResource(R.string.email_label) + " " + user?.email,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    text = stringResource(R.string.musixpoints_label) + " ${userObj.points}",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
                TextButton(
                    modifier = Modifier
                        .padding(20.dp)
                        .width(200.dp),
                    onClick = {
                       logout(viewModel, activity)
                        val intent = Intent(
                            activity,
                            SigninActivity::class.java
                        )
                        startActivity(activity, intent, null)

                    },
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = Color.White
                    ), enabled = true
                ) {
                    Text(text = stringResource(R.string.logout_button))
                }
            }
        }
    }
}


private fun logout(viewModel: ProfileViewModel, activity: Activity) {
    //TODO: use AuthRepository (function below)
    /* viewModel.logout().observeForever {
        when (it) {
            is Result.Success -> {
                val intent = Intent(
                    activity,
                    SigninActivity::class.java
                )
                startActivity(activity, intent, null)
            }
            is Result.Error -> {
                Toast.makeText(
                    activity, "Logout failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    } */
    Firebase.auth.signOut()
}