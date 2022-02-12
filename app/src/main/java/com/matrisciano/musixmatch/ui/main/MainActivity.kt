package com.matrisciano.musixmatch.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.ui.main.home.HomeScreen
import com.matrisciano.musixmatch.ui.main.leaderboard.LeaderboardScreen
import com.matrisciano.musixmatch.ui.main.profile.ProfileScreen
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.util.*
import kotlin.collections.HashMap

class MainActivity : ComponentActivity() {
    private var leaderboard = hashMapOf<String, Long>()
    private var points: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = Firebase.auth //TODO: create FirebaseAuth Repository
        val currentUser = auth.currentUser

        getUsersInfo(currentUser)

        setContent {
            val navCtrl = rememberNavController()
            //val viewModel = getViewModel<MainViewModel>()
            //val scope = rememberCoroutineScope()

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
        object Home :
            BottomNavItem("Home", R.drawable.ic_home, "home_screen")

        object Leaderboard :
            BottomNavItem("Leaderboard", R.drawable.ic_leaderboard, "leaderboard_screen")

        object Profile :
            BottomNavItem("Profile", R.drawable.ic_profile, "profile_screen")
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

    private fun getUsersInfo(user: FirebaseUser?) {
        val db = Firebase.firestore //TODO: create Firestore Repository
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Firestore", "${document.id} => ${document.data}")
                    if (document.data["email"] == user?.email)
                        points = document.data["points"] as Long
                    leaderboard[document.data["email"] as String] = document.data["points"] as Long
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
}