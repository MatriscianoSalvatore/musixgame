package com.matrisciano.musixmatch.ui

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.ui.home.HomeScreen
import com.matrisciano.musixmatch.ui.leaderboard.LeaderboardScreen
import com.matrisciano.musixmatch.ui.profile.ProfileScreen
import com.matrisciano.musixmatch.ui.signin.SigninActivity
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.musixmatch.ui.theme.musixmatchPinkLight
import com.matrisciano.network.model.TrackID
import com.matrisciano.network.network.Network
import com.matrisciano.network.service.MusixmatchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.Exception
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private var leaderboard = hashMapOf<String, Long>()
    private var points: Long = 0
    private val maxTracks = 45; //divisible for matchesNumber
    private val matchesNumber = 3
    private var correctIndexes = Array<Int?>(matchesNumber) { null }
    private var artists = Array(matchesNumber) { arrayOf("", "", "") }
    private var tracks = Array<String?>(matchesNumber) { null }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        val currentUser = auth.currentUser


        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    if (document.data["email"] == currentUser?.email)
                        points = document.data["points"] as Long
                    leaderboard.put(
                        document.data["email"] as String,
                        document.data["points"] as Long
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        setContent {
            var navCtrl = rememberNavController()
            MusixmatchTheme()
            {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Scaffold(
                        bottomBar = { BottomNavigation(navCtrl = navCtrl) },
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Musixgame")
                                },
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White,
                                elevation = 12.dp
                            )
                        }, content = {}
                    )
                }
            }
            Navigation(navCtrl, currentUser, leaderboard)
        }
    }







    sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
        object Home : BottomNavItem("Home", R.drawable.ic_home, "home_screen")
        object Leaderboard :
            BottomNavItem("Leaderboard", R.drawable.ic_leaderboard, "leaderboard_screen")

        object Profile : BottomNavItem("Profile", R.drawable.ic_profile, "profile_screen")
    }

    @Composable
    fun BottomNavigation(navCtrl: NavController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Leaderboard,
            BottomNavItem.Profile
        )
        BottomNavigation(
            backgroundColor = Color.White
        ) {
            val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title
                        )
                    },
                    label = { Text(text = item.title) },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = Color.Black.copy(0.4f),
                    alwaysShowLabel = true,
                    selected = currentRoute == item.screen_route,
                    onClick = {
                        navCtrl.navigate(item.screen_route)
                    }
                )
            }
        }
    }

    @Composable
    fun Navigation(navCtrl: NavHostController, user: FirebaseUser?, leaderboard: HashMap<String, Long>) {
        NavHost(navCtrl, "home_screen") {
            composable("home_screen") {
                HomeScreen(this@MainActivity)
            }
            composable("leaderboard_screen") {
                LeaderboardScreen(user, leaderboard)
            }
            composable("profile_screen") {
                ProfileScreen(user, this@MainActivity, points)
            }
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun ProfilePreview() {
        ProfileScreen(Firebase.auth.currentUser, this@MainActivity, points)
    }


    /*private fun getTrackID(artist: String, title: String) {
        val network = Network().createServiceApi(MusixmatchService::class)
        val call = network.getTrackID(artist, title, 1, "desc", "desc")
        call.enqueue(object : Callback<TrackID> {
            override fun onResponse(
                call: Call<TrackID>,
                response: Response<TrackID>
            ) {
                if (response.code() == 200) {
                    try {
                        val trackID = response.body()?.message?.body?.track_list?.get(0)?.track?.track_id
                        if (trackID != null) {
                            //TODO: getLyrics(trackID)
                            Log.d("MainActivity", "trackID: $trackID")
                        } else showTrackNotFoundToast()
                    } catch (e: Exception) {
                        showTrackNotFoundToast()
                    }
                } else showTrackNotFoundToast()
            }

            override fun onFailure(call: Call<TrackID>, t: Throwable) {
                showTrackNotFoundToast()
            }
        })
    }*/


/*

    fun getTopTracks() {



        //TODO: remove
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





        val service = retrofit.create(Api.GetTopTracks::class.java)
        val call =
            service.getTopTracks("top", 1, maxTracks, "it", 1, "4ac3d61572388ffbcb08f9e160fec313")
        call.enqueue(object : Callback<Api.TopTracksResponse> {
            override fun onResponse(
                call: Call<Api.TopTracksResponse>,
                response: Response<Api.TopTracksResponse>
            ) {
                if (response.code() == 200) {
                    try {
                        var topTracks = Gson().newBuilder().disableHtmlEscaping().create()
                            .toJson(response.body()?.message?.body?.track_list)
                        if (topTracks != null) {


                            var indexes = Array(matchesNumber) { arrayOf(0, 1, 2) }
                            for (i in 0 until matchesNumber) {

                                indexes[i][0] = ((maxTracks / matchesNumber * i) until ((maxTracks / matchesNumber * (i+1)) )).random()
                                while (artists[i][0] == artists[i][1] || artists[i][1] == artists[i][2] || artists[i][0] == artists[i][2]) {

                                    indexes[i][1] = (0 until maxTracks).random()
                                    indexes[i][2] = (0 until maxTracks).random()

                                    tracks[i] = Gson().newBuilder().disableHtmlEscaping().create()
                                        .toJson(
                                            response.body()?.message?.body?.track_list?.get(
                                                indexes[i][0]
                                            )?.track?.track_id
                                        )

                                    for (j in 0..2)
                                        artists[i][j] =
                                            Gson().newBuilder().disableHtmlEscaping().create()
                                                .toJson(
                                                    response.body()?.message?.body?.track_list?.get(
                                                        indexes[i][j]
                                                    )?.track?.artist_name
                                                )
                                }

                                var correctArtist = artists[i][0]
                                artists[i].shuffle()
                                correctIndexes[i] = artists[i].indexOf(correctArtist)
                            }

                            getSnippet(tracks[0]!!)

                        } else showTrackNotFoundToast()
                    } catch (e: Exception) {
                        showTrackNotFoundToast()
                    }
                } else showTrackNotFoundToast()
            }

            override fun onFailure(call: Call<Api.TopTracksResponse>, t: Throwable) {
                showTrackNotFoundToast()
            }
        })
    }


    fun getSnippet(trackID: String) {
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

                        var snippet = Gson().newBuilder().disableHtmlEscaping().create()
                            .toJson(response.body()?.message?.body?.snippet?.snippet_body)

                        if (snippet != null) {

                            val intent = Intent(this@MainActivity, WhoSingsActivity::class.java)
                            for (i in 0 until matchesNumber) {
                                for (j in 0..2) intent.putExtra(
                                    "artist" + i + "_" + j,
                                    artists[i][j]
                                )
                                intent.putExtra("correctIndex$i", correctIndexes[i])
                                intent.putExtra("track$i", tracks[i])
                                intent.putExtra("snippet", snippet)
                            }
                            startActivity(intent)

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
    }*/


    fun showTrackNotFoundToast() {
        Toast.makeText(
            baseContext, "Track not found",
            Toast.LENGTH_SHORT
        ).show()
    }
}