package com.matrisciano.musixmatch.ui.main.profile

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
<<<<<<< HEAD
import androidx.compose.runtime.rememberCoroutineScope
=======
import androidx.compose.runtime.livedata.observeAsState
>>>>>>> fix/firebase_repositories
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.ui.main.MainActivity
import com.matrisciano.musixmatch.ui.signin.SigninActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
<<<<<<< HEAD
import kotlinx.coroutines.launch
=======
import com.matrisciano.network.utils.Result
>>>>>>> fix/firebase_repositories
import org.koin.androidx.compose.getViewModel

@Composable
fun ProfileScreen(user: FirebaseUser?, activity: MainActivity, points: Long) {
<<<<<<< HEAD
    val viewModel = getViewModel<ProfileViewModel>()
=======

    val viewModel = getViewModel<UserViewModel>()
    Log.d("FIREBASE_TAG", "FIREBASE USER: ${user?.uid}")

    viewModel.getUser("GIABBA_BOY").observeAsState().value.let {
        if (it is Result.Success) {
            Log.d("FIREBASE_TAG", "FIREBASE VIEWMODEL: ${it.value.email}")
        }
    }

    MusixmatchTheme() {
>>>>>>> fix/firebase_repositories


    //val scope = rememberCoroutineScope()
    //scope.launch { viewModel.getUsers() }
    //val users = viewModel.getUsers()

    //when (val booksList = viewModel.getUsers().

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
                    "Name: " + user?.displayName,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    "Email: " + user?.email,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
                Text(
                    "Musixpoints: $points",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(2.dp)
                )
                TextButton(
                    modifier = Modifier
                        .padding(20.dp)
                        .width(200.dp),
                    onClick = {
                        Firebase.auth.signOut()

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
                    Text(text = "LOGOUT")
                }
            }
        }
    }
}
