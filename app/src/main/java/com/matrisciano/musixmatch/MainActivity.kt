package com.matrisciano.musixmatch

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import com.matrisciano.musixmatch.ui.theme.musixmatchPinkLight

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val leaderboard = hashMapOf<String, Long>()
    private var points: Long = 0

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
            Navigation(navCtrl, currentUser)
        }
    }

    @Composable
    fun HomeScreen() {
        MusixmatchTheme() {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    var title by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        title,
                        onInputChanged = { title = it },
                        hint = "Title",
                    )

                    var artist by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        artist,
                        onInputChanged = { artist = it },
                        hint = "Artist",
                    )

                    TextButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .width(200.dp),
                        onClick = {
                            startActivity(Intent(this@MainActivity, GameActivity::class.java))
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = true
                    ) {
                        Text(text = "PLAY")
                    };

                }
            }
        }
    }

    @Composable
    fun LeaderboardScreen() {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(6.dp, 58.dp, 0.dp, 0.dp)
                .scrollable(      state = scrollState,
                    orientation = Orientation.Vertical )

        ) {

            var sortedLeaderboard: MutableMap<String, Long> = LinkedHashMap()
            leaderboard.entries.sortedByDescending { it.value }
                .forEach { sortedLeaderboard[it.key] = it.value }

            var i = 0
            for (element in sortedLeaderboard) {
                i++
                Text(i.toString() + "°- " + element.key + ": " + element.value + " musixpoints",
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp,
                )
            }
        }
    }

    @Composable
    fun ProfileScreen(user: FirebaseUser?) {
        MusixmatchTheme() {

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
                            startActivity(Intent(this@MainActivity, SigninActivity::class.java))
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = true
                    ) {
                        Text(text = "LOGOUT")
                    };
                }
            }
        }
    }

    sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
        object Home : BottomNavItem("Home", R.drawable.ic_home, "home_screen")
        object Leaderboard : BottomNavItem("Leaderboard", R.drawable.ic_leaderboard, "leaderboard_screen")
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
    fun Navigation(navCtrl: NavHostController, user: FirebaseUser?) {
        NavHost(navController = navCtrl, startDestination = "home_screen") {
            composable("home_screen") {
                HomeScreen()
            }
            composable("leaderboard_screen") {
                LeaderboardScreen()
            }
            composable("profile_screen") {
                ProfileScreen(user)
            }
        }
    }

    @Composable
    fun MusixGameTextField(
        value: String,
        onInputChanged: (String) -> Unit,
        hint: String,
    ) {
        val focusManager = LocalFocusManager.current
        TextField(
            value = value,
            maxLines = 1,
            singleLine = true,
            onValueChange = onInputChanged,
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = musixmatchPinkLight,
                textColor = Color(0xFFFFFFFF),
                unfocusedLabelColor = Color(0x70FFFFFF),
            ),
            label = { Text(hint) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
    }

    @Preview(showBackground = true)
    @Composable
    fun ProfilePreview() {
        ProfileScreen(Firebase.auth.currentUser)
    }
}