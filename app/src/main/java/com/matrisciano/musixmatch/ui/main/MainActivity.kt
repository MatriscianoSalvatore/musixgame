package com.matrisciano.musixmatch.ui.main

import android.os.Bundle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.ui.main.home.HomeScreen
import com.matrisciano.musixmatch.ui.main.leaderboard.LeaderboardScreen
import com.matrisciano.musixmatch.ui.main.profile.ProfileScreen
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme

import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navCtrl = rememberNavController()
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
                                    Text(text = stringResource(R.string.app_name))
                                },
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White,
                                elevation = 12.dp
                            )
                        }, content = {}
                    )
                }
            }
            Navigation(navCtrl)
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
    fun Navigation(navCtrl: NavHostController) {
        NavHost(navCtrl, "home_screen") {
            composable("home_screen") {
                HomeScreen(this@MainActivity)
            }
            composable("leaderboard_screen") {
                LeaderboardScreen()
            }
            composable("profile_screen") {
                ProfileScreen(this@MainActivity)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ProfilePreview() {
        ProfileScreen(this@MainActivity)
    }
}