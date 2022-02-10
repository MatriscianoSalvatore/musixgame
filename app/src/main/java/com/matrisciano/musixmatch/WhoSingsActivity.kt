package com.matrisciano.musixmatch

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
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.loseRed
import com.matrisciano.musixmatch.ui.theme.winGreen
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.*

class WhoSingsActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var snippet: String? = ""
    private val maxArtistChars = 55
    private val matchesNumber = 3
    var tracks =  Array<String?>(matchesNumber) { null }
    private var correctIndexes =  Array<Int?>(matchesNumber) { null }
    private var artists = Array(matchesNumber) { arrayOf("", "", "") }
    private var step = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser


        for (i in 0 until matchesNumber) {
            for (j in 0..2) artists[i][j] = getIntent().getStringExtra("artist" + i + "_" + j)!!
            tracks[i] = getIntent().getStringExtra("track$i")
            correctIndexes[i] = getIntent().getIntExtra("correctIndex$i", 0)
        }



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

        getSnippet(tracks[step]!!)


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
                                                if (correctIndexes[step] == i) {
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
                            if (artists[step][i].length > maxArtistChars) {
                                artists[step][i] = artists[step][i]!!.substring(0, maxArtistChars)
                                artists[step][i] = "$artists[i]..."
                            }
                            Text(text = artists[step][i]!!.replace("\"", ""), fontSize = 18.sp)
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











    interface GetSnippet {
        @Headers("apikey: " + "276b2392f053c47db5b3b5f072f54aa7")
        @GET("track.snippet.get")
        fun getCurrentTrackData(
            @Query("track_id") track_id: String,
            @Query("apikey") apikey: String
        ): Call<SnippetResponse>
    }

    class SnippetResponse {
        @SerializedName("message")
        var message: SnippetMessage? = null
    }

    class SnippetMessage {
        @SerializedName("body")
        var body: SnippetBody? = null
    }

    class SnippetBody {
        @SerializedName("snippet")
        var snippet: SnippetLyrics? = null
    }

    class SnippetLyrics {
        @SerializedName("snippet_body")
        var snippet_body: String? = null
    }

    fun getSnippet(trackID: String) {
        var okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("apikey", "276b2392f053c47db5b3b5f072f54aa7")
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.musixmatch.com/ws/1.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(GetSnippet::class.java)
        val call = service.getCurrentTrackData(trackID, "276b2392f053c47db5b3b5f072f54aa7")
        call.enqueue(object : Callback<SnippetResponse> {
            override fun onResponse(
                call: Call<SnippetResponse>,
                response: Response<SnippetResponse>
            ) {
                if (response.code() == 200) {
                    try {

                        var snippet = Gson().newBuilder().disableHtmlEscaping().create()
                            .toJson(response.body()?.message?.body?.snippet?.snippet_body)

                        if (snippet != null) {
                             //TODO:
                        } else showTrackNotFoundToast()
                    } catch (e: Exception) {
                        showTrackNotFoundToast()
                    }
                } else showTrackNotFoundToast()
            }

            override fun onFailure(call: Call<SnippetResponse>, t: Throwable) {
                showTrackNotFoundToast()
            }
        })

    }





    fun showTrackNotFoundToast() {
        Toast.makeText(
            baseContext, "Track not foundd ",
            Toast.LENGTH_SHORT
        ).show()
    }










}
