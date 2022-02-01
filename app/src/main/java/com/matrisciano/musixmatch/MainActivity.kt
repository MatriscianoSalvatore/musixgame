package com.matrisciano.musixmatch

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.ui.theme.MusixmatchTheme
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }

    }

    @Composable
    fun LauncherScreen(navCtrl: NavController) {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navCtrl.navigate("home_screen");
        }

        MusixmatchPinkTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Image(painterResource(R.mipmap.logo), "Musixgame")


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextButton(
                            modifier = Modifier
                                .padding(3.dp)
                                .width(200.dp),
                            onClick = { navCtrl.navigate("login_screen") },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ), enabled = true
                        ) {

                            Text(text = "LOGIN")

                        };
                        TextButton(
                            modifier = Modifier
                                .padding(3.dp)
                                .width(200.dp),
                            onClick = {
                                navCtrl.navigate("signup_screen")
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = Color.White
                            ), enabled = true
                        ) {

                            Text(text = "SIGNUP")
                        }
                    }
                }


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


    @Composable
    fun LoginScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {


                    var email by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        email,
                        onInputChanged = { email = it },
                        hint = "email",
                        TextfieldType.EMAIL
                    )

                    var password by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        password,
                        onInputChanged = { password = it },
                        hint = "password",
                        TextfieldType.PASSWORD
                    )

                    TextButton(
                        modifier = Modifier
                            .padding(0.dp, 15.dp, 0.dp, 0.dp)
                            .width(200.dp),
                        onClick = { if (email != null && password != null) Login(email, password, navCtrl) },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = true
                    ) {

                        Text(text = "LOGIN")

                    };


                }
            }
        }
    }

    @Composable
    fun SignupScreen(navCtrl: NavController) {
        MusixmatchPinkTheme() {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center

            ) {


                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {


                    var name by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        name,
                        onInputChanged = { name = it },
                        hint = "name and surname",
                        TextfieldType.TEXT
                    )

                    var email by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        email,
                        onInputChanged = { email = it },
                        hint = "email",
                        TextfieldType.EMAIL
                    )

                    var password by rememberSaveable { mutableStateOf("") }
                    MusixGameTextField(
                        password,
                        onInputChanged = { password = it },
                        hint = "password",
                        TextfieldType.PASSWORD
                    )



                    TextButton(
                        modifier = Modifier
                            .padding(0.dp, 15.dp, 0.dp, 0.dp)
                            .width(200.dp),
                        onClick = { if (email != null && password != null) Signup(email, password, navCtrl) },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = true
                    ) {

                        Text(text = "SIGNUP")

                    }

                }


            }
        }
    }


    @Composable
    fun MusixGameTextField(
        value: String,
        onInputChanged: (String) -> Unit,
        hint: String,
        textfieldType: TextfieldType
    ) {
        TextField(
            value = value,
            onValueChange = onInputChanged,
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFFFFFFFF),
                unfocusedLabelColor = Color(0x70FFFFFF),
            ),
            label = { Text(hint) },
            visualTransformation = if (textfieldType == TextfieldType.PASSWORD) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (textfieldType == TextfieldType.PASSWORD) KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
            else if (textfieldType == TextfieldType.EMAIL) KeyboardOptions(keyboardType = KeyboardType.Email)
            else KeyboardOptions(keyboardType = KeyboardType.Text),
        )
    }


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
            composable("signup_screen") {
                SignupScreen(navCtrl = navCtrl)
            }
            composable("login_screen") {
                LoginScreen(navCtrl = navCtrl)
            }
            composable("home_screen") {
                HomeScreen(navCtrl = navCtrl)
            }
        }
    }

    fun Signup(email: String, password: String, navCtrl: NavController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    navCtrl.navigate("home_screen")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    fun Login(email: String, password: String, navCtrl: NavController) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    navCtrl.navigate("home_screen")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    @Preview(showBackground = true)
    @Composable
    fun LauncherPreview() {
        LauncherScreen(rememberNavController())
    }

    @Preview(showBackground = true)
    @Composable
    fun LoginPreview() {
        LoginScreen(rememberNavController())
    }

    @Preview(showBackground = true)
    @Composable
    fun HomePreview() {
        HomeScreen(rememberNavController())
    }

    enum class TextfieldType {
        TEXT, EMAIL, PASSWORD
    }

}
