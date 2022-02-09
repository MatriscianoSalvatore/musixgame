package com.matrisciano.musixmatch

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.util.Log
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.loseRed
import com.matrisciano.musixmatch.ui.theme.winGreen
import java.util.*

class WhoSingsActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var snippet: String? = ""
    private var artist1: String? = ""
    private var artist2: String? = ""
    private var artist3: String? = ""
    private var correctIndex = 0
    private var maxArtistChar = 55
    private var artists: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        snippet = getIntent().getStringExtra("snippet")
        artist1 = getIntent().getStringExtra("artist1")
        artist2 = getIntent().getStringExtra("artist2")
        artist3 = getIntent().getStringExtra("artist3")
        correctIndex = getIntent().getIntExtra("correctIndex", 0)
        artists.add(artist1!!)
        artists.add(artist2!!)
        artists.add(artist3!!)

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
                        text = snippet!!,
                        color = Color.White,
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(15.dp)
                    )

                    for (i in 0..2) {
                        TextButton(
                            modifier = Modifier
                                .width(400.dp)
                                .padding(28.dp),
                            onClick = {
                                val db = Firebase.firestore
                                var points: Long
                                db.collection("users")
                                    .get()
                                    .addOnSuccessListener { result ->
                                        for (document in result) {
                                            Log.d(
                                                ControlsProviderService.TAG,
                                                "${document.id} => ${document.data}"
                                            )
                                            if (document.data["email"] == user?.email) {
                                                points = document.data["points"] as Long
                                                if (correctIndex == i) {
                                                    db.collection("users").document(document.id)
                                                        .update("points", points + 5)
                                                    navCtrl.navigate("win_screen")
                                                } else {
                                                    db.collection("users").document(document.id)
                                                        .update("points", points - 1)
                                                    navCtrl.navigate("lose_screen")
                                                }
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w( //TODO: show error toast
                                            ControlsProviderService.TAG,
                                            "Error getting documents.",
                                            exception
                                        )
                                    }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ),
                            enabled = true
                        ) {
                            if (artists[i].length > maxArtistChar) {
                                artists[i] = artists[i]!!.substring(0, maxArtistChar)
                                artists[i] = "$artists[i]..."
                            }
                            Text(text = artists[i]!!.replace("\"", ""), fontSize = 18.sp)
                        };
                    }
                }
            }
        }
    }

    @Composable
    fun WinScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
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
                        text = "Congratulations, you won 5 Musixpoints!",
                        color = Color.White,
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(30.dp)
                    )

                    TextButton(
                        modifier = Modifier
                            .width(400.dp)
                            .padding(28.dp),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@WhoSingsActivity,
                                    MainActivity::class.java
                                )
                            )
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = true
                    ) {
                        Text(text = "OK", fontSize = 18.sp)
                    }
                }
            }
        }
    }

    @Composable
    fun LoseScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
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
                        text = "Wrong answer, you lost 1 Musixpoint!",
                        color = Color.White,
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(30.dp)
                    )

                    TextButton(
                        modifier = Modifier
                            .width(400.dp)
                            .padding(28.dp),
                        onClick = {
                            startActivity(
                                Intent(
                                    this@WhoSingsActivity,
                                    MainActivity::class.java
                                )
                            )
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = true
                    ) {
                        Text(text = "OK", fontSize = 18.sp)
                    };
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
                WinScreen(navCtrl)
            }
            composable("lose_screen") {
                LoseScreen(navCtrl)
            }
        }
    }

}
