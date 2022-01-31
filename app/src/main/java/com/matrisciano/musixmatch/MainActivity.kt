package com.matrisciano.musixmatch

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }

    @Composable
    fun LauncherScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Image(painterResource(R.mipmap.logo),"Musixgame")


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextButton(
                            modifier = Modifier.padding(2.dp),
                            onClick = {
                                navCtrl.navigate("home_screen")
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = Color.White
                            ), enabled = true
                        ) {

                            Text(text = "LOGIN")
                        };
                        TextButton(
                            modifier = Modifier.padding(2.dp),
                            onClick = {
                                navCtrl.navigate("home_screen")
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.secondary,
                                contentColor = Color.White
                            ), enabled = true
                        ) {

                            Text(text = "SIGNUP")
                        }
                    }
                }


                /*Surface(color = MaterialTheme.colors.primary) {


            }*/


            }
        }
    }

    @Composable
    fun HomeScreen(navCtrl: NavController) {
        MusixmatchTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Musixgame")
                            },

                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White,
                            elevation = 12.dp
                        )
                    }, content = {

                    })


            }
        }
    }

    /*   @Composable
       fun SplashScreen(navCtrl: NavController) {
           MusixmatchPinkTheme() {
               Box(
                   modifier = Modifier
                       .background(MaterialTheme.colors.primary)
                       .fillMaxSize(),
                   contentAlignment = Alignment.Center

               ) {
                   //navCtrl.navigate("launcher_screen")
               }
           }
       }*/

    @Composable
    fun Navigation() {
        val navCtrl = rememberNavController()
        NavHost(navController = navCtrl, startDestination = "launcher_screen") {
            /*  composable("splash_screen") {
                  SplashScreen(navCtrl = navCtrl)
              }*/
            composable("launcher_screen") {
                LauncherScreen(navCtrl = navCtrl)
            }
            composable("home_screen") {
                HomeScreen(navCtrl = navCtrl)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        LauncherScreen(rememberNavController())
    }

    @Preview(showBackground = true)
    @Composable
    fun SecondPreview() {
        HomeScreen(rememberNavController())
    }

}
