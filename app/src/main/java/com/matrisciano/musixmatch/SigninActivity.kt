package com.matrisciano.musixmatch

import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme

class SigninActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val currentUser = auth.currentUser

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
    fun LauncherScreen(navCtrl: NavController) {
        MusixmatchPinkTheme()
        {
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
                    SigninTextField(
                        email,
                        onInputChanged = { email = it },
                        hint = "Email",
                        TextfieldType.EMAIL
                    )

                    var password by rememberSaveable { mutableStateOf("") }
                    SigninTextField(
                        password,
                        onInputChanged = { password = it },
                        hint = "Password",
                        TextfieldType.PASSWORD
                    )

                    TextButton(
                        modifier = Modifier
                            .padding(0.dp, 15.dp, 0.dp, 0.dp)
                            .width(200.dp),
                        onClick = {
                            if (email != "" && password != "")
                                login(
                                    email,
                                    password,
                                    navCtrl
                                )
                        },
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
                    SigninTextField(
                        name,
                        onInputChanged = { name = it },
                        hint = "Name and surname",
                        TextfieldType.TEXT
                    )

                    var email by rememberSaveable { mutableStateOf("") }
                    SigninTextField(
                        email,
                        onInputChanged = { email = it },
                        hint = "Email",
                        TextfieldType.EMAIL
                    )

                    var password by rememberSaveable { mutableStateOf("") }
                    SigninTextField(
                        password,
                        onInputChanged = { password = it },
                        hint = "Password",
                        TextfieldType.PASSWORD
                    )

                    TextButton(
                        modifier = Modifier
                            .padding(0.dp, 15.dp, 0.dp, 0.dp)
                            .width(200.dp),
                        onClick = {
                            if (email != "" && password != "")
                                signup(
                                    name,
                                    email,
                                    password,
                                    navCtrl
                                )
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

    @Composable
    fun SigninTextField(
        value: String,
        onInputChanged: (String) -> Unit,
        hint: String,
        textfieldType: TextfieldType
    ) {
        TextField(
            value = value,
            maxLines = 1,
            onValueChange = onInputChanged,
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color(0xFFFFFFFF),
                unfocusedLabelColor = Color(0x70FFFFFF),
            ),
            label = { Text(hint) },
            visualTransformation = if (textfieldType == TextfieldType.PASSWORD) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = when (textfieldType) {
                TextfieldType.PASSWORD -> KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
                TextfieldType.EMAIL -> KeyboardOptions(keyboardType = KeyboardType.Email)
                else -> KeyboardOptions(keyboardType = KeyboardType.Text)
            },
        )
    }

    fun signup(name: String, email: String, password: String, navCtrl: NavController) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    auth.currentUser?.updateProfile(UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build())

                    val db = Firebase.firestore
                    val user = hashMapOf(
                        "email" to email,
                        "points" to 0,
                    )
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                    startActivity(Intent(this@SigninActivity, MainActivity::class.java))

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

    fun login(email: String, password: String, navCtrl: NavController) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    // Start main activity
                    startActivity(Intent(this@SigninActivity, MainActivity::class.java))

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

    @Composable
    fun Navigation(user: FirebaseUser?) {
        val navCtrl = rememberNavController()
        if (user != null)   startActivity(Intent(this@SigninActivity, MainActivity::class.java)) else {
            NavHost(navController = navCtrl, startDestination = "launcher_screen") {
                composable("launcher_screen") {
                    LauncherScreen(navCtrl = navCtrl)
                }
                composable("signup_screen") {
                    SignupScreen(navCtrl = navCtrl)
                }
                composable("login_screen") {
                    LoginScreen(navCtrl = navCtrl)
                }
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun LoginPreview() {
        LoginScreen(rememberNavController())
    }

    enum class TextfieldType {
        TEXT, EMAIL, PASSWORD
    }
}
