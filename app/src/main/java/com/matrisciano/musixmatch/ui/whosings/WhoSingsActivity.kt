package com.matrisciano.musixmatch.ui.whosings

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService
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
import com.matrisciano.musixmatch.ui.main.MainActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.loseRed
import com.matrisciano.musixmatch.ui.theme.winGreen
import com.matrisciano.network.utils.Result
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

class WhoSingsActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var snippet: String? = ""
    private val maxArtistChars = 55
    private val matchesNumber = 3
    var tracks = Array<String?>(matchesNumber) { null }
    private var correctIndexes = Array<Int?>(matchesNumber) { null }
    private var artists = Array(matchesNumber) { arrayOf("", "", "") }
    private var currentMatch = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        for (i in 0 until matchesNumber) {
            for (j in 0..2) artists[i][j] = getIntent().getStringExtra("artist" + i + "_" + j)!!
            tracks[i] = getIntent().getStringExtra("track$i")
            correctIndexes[i] = getIntent().getIntExtra("correctIndex$i", 0)
        }
        snippet = getIntent().getStringExtra("snippet")


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
                        modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 25.dp)
                    )

                    for (i in 0..2) {
                        TextButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
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
                                                if (correctIndexes[currentMatch] == i) {
                                                    db.collection("users").document(document.id)
                                                        .update("points", points + 5)
                                                    navCtrl.navigate("win_screen") {
                                                        popUpTo("game_screen") {
                                                            inclusive = true
                                                        }
                                                    }
                                                } else {
                                                    db.collection("users").document(document.id)
                                                        .update("points", points - 1)
                                                    navCtrl.navigate("lose_screen") {
                                                        popUpTo("game_screen") {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Toast.makeText(
                                            baseContext, "Database error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ),
                            enabled = true
                        ) {
                            if (artists[currentMatch][i].length > maxArtistChars) {
                                artists[currentMatch][i] =
                                    artists[currentMatch][i]!!.substring(0, maxArtistChars)
                                artists[currentMatch][i] = "$artists[i]..."
                            }
                            Text(
                                text = artists[currentMatch][i]!!.replace("\"", ""),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        };
                    }
                }
            }
        }
    }

    @Composable
    fun WinScreen(navCtrl: NavController) {
        val viewModel = getViewModel<WhoSingsViewModel>()
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

                    val scope = rememberCoroutineScope()

                    TextButton(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(28.dp),

                        onClick = {
                            scope.launch { nextStep(viewModel, navCtrl) }
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
        val viewModel = getViewModel<WhoSingsViewModel>()
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

                    val scope = rememberCoroutineScope()

                    TextButton(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(28.dp),
                        onClick = {
                            scope.launch { nextStep(viewModel, navCtrl) }
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


/*    private fun getSnippet(trackID: String, navCtrl: NavController) {
        var okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("apikey", "4ac3d61572388ffbcb08f9e160fec313")
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.musixmatch.com/ws/1.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(Api.GetSnippet::class.java)
        val call = service.getCurrentTrackData(trackID, "4ac3d61572388ffbcb08f9e160fec313")
        call.enqueue(object : Callback<Api.SnippetResponse> {
            override fun onResponse(
                call: Call<Api.SnippetResponse>,
                response: Response<Api.SnippetResponse>
            ) {
                if (response.code() == 200) {
                    try {

                        var currentSnippet = Gson().newBuilder().disableHtmlEscaping().create()
                            .toJson(response.body()?.message?.body?.snippet?.snippet_body)

                        if (currentSnippet != null) {
                            snippet = currentSnippet
                            navCtrl.navigate("game_screen") {
                                popUpTo("game_screen") {
                                    inclusive = true
                                }
                            }

                        } else showTrackNotFoundToast()
                    } catch (e: Exception) {
                        showTrackNotFoundToast()
                    }
                } else showTrackNotFoundToast()
            }

            override fun onFailure(call: Call<Api.SnippetResponse>, t: Throwable) {
                showTrackNotFoundToast()
            }
        })
    } */

    private suspend fun nextStep(
        viewModel: WhoSingsViewModel?,
        navCtrl: NavController
    ) {
        currentMatch++
        if (currentMatch < matchesNumber) {

            when (val result = viewModel?.getSnippet(tracks[currentMatch]!!)) {
                is Result.Success -> {
                    val currentSnippet =
                        result.value.message?.body?.snippet?.snippet_body
                    Log.d("WhoSingActivity", "Snipptet: $currentSnippet")
                    snippet = currentSnippet
                    navCtrl.navigate("game_screen") {
                        popUpTo("game_screen") {
                            inclusive = true
                        }
                    }
                }

                is Result.Error -> {
                    Log.d("WhoSingActivity", "Snipptet error: ${result.message}")
                }
            }


        } else startActivity(
            Intent(
                this@WhoSingsActivity,
                MainActivity::class.java
            )
        )
    }


    fun showTrackNotFoundToast() {
        Toast.makeText(
            baseContext, "Track not found",
            Toast.LENGTH_SHORT
        ).show()
    }
}