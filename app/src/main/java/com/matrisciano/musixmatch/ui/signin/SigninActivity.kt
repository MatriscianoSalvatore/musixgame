package com.matrisciano.musixmatch.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.matrisciano.musixmatch.ui.main.MainActivity
import com.matrisciano.musixmatch.R
import com.matrisciano.musixmatch.component.SigninTextField
import com.matrisciano.musixmatch.ui.theme.MusixmatchPinkTheme
import com.matrisciano.musixmatch.utils.Preferences
import com.matrisciano.musixmatch.utils.Preferences.set
import com.matrisciano.network.utils.Result
import org.koin.androidx.compose.getViewModel

class SigninActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            Navigation()
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
                    Image(painterResource(R.mipmap.logo), stringResource(R.string.app_name))
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
                            Text(text = stringResource(R.string.login_button))
                        }
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
                            Text(text = stringResource(R.string.signup_button))
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LoginScreen() {
        val viewModel = getViewModel<SigninViewModel>()
        MusixmatchPinkTheme {
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
                        hint = stringResource(R.string.email_hint),
                        TextfieldType.EMAIL
                    )

                    var password by rememberSaveable { mutableStateOf("") }
                    SigninTextField(
                        password,
                        onInputChanged = { password = it },
                        hint = stringResource(R.string.password_hint),
                        TextfieldType.PASSWORD
                    )

                    TextButton(
                        modifier = Modifier
                            .padding(0.dp, 15.dp, 0.dp, 0.dp)
                            .width(200.dp),
                        onClick = {
                            if (email != "" && password != "")
                                login(
                                    viewModel,
                                    email,
                                    password,
                                    baseContext
                                )
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = (email != "" && password != "")
                    ) {
                        Text(text = stringResource(R.string.login_button))
                    }
                }
            }
        }
    }

    @Composable
    fun SignupScreen() {
        val viewModel = getViewModel<SigninViewModel>()
        MusixmatchPinkTheme {
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
                        hint = stringResource(R.string.name_hint),
                        TextfieldType.TEXT
                    )

                    var email by rememberSaveable { mutableStateOf("") }
                    SigninTextField(
                        email,
                        onInputChanged = { email = it },
                        hint = stringResource(R.string.email_hint),
                        TextfieldType.EMAIL
                    )

                    var password by rememberSaveable { mutableStateOf("") }
                    SigninTextField(
                        password,
                        onInputChanged = { password = it },
                        hint = stringResource(R.string.password_hint),
                        TextfieldType.PASSWORD
                    )

                    TextButton(
                        modifier = Modifier
                            .padding(0.dp, 15.dp, 0.dp, 0.dp)
                            .width(200.dp),
                        onClick = {
                            if (email != "" && password != "")
                                signup(
                                    viewModel,
                                    name,
                                    email,
                                    password,
                                    baseContext
                                )
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White
                        ), enabled = (name != "" && email != "" && password != "")
                    ) {
                        Text(text = stringResource(R.string.signup_button))
                    }
                }
            }
        }
    }

    private fun signup(
        viewModel: SigninViewModel,
        name: String,
        email: String,
        password: String,
        context: Context
    ) {
        viewModel.signup(name, email, password).observeForever { result ->
            when (result) {
                is Result.Success -> {
                    result.value?.let {
                        Log.d("Login user", "Login User: $it")
                        Preferences.defaultPref(context)["userID"] = it.uid
                        viewModel.createUser(it.uid, name, email).observeForever {
                            startActivity(Intent(this@SigninActivity, MainActivity::class.java))
                        }
                    }
                }
                is Result.Error -> {
                    Toast.makeText(
                        baseContext, "Registration failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun login(
        viewModel: SigninViewModel,
        email: String,
        password: String,
        context: Context
    ) {
        viewModel.login(email, password).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    result.value?.let {
                        Log.d("Login user", "Login User: $it")
                        Preferences.defaultPref(context)["userID"] = it.uid
                        startActivity(Intent(this@SigninActivity, MainActivity::class.java))
                    }
                }
                is Result.Error -> {
                    Toast.makeText(
                        baseContext, "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    @Composable
    private fun Navigation() {
        val navCtrl = rememberNavController()
        if (Preferences.defaultPref(baseContext).getString("userID", null) != null) startActivity(
            Intent(
                this@SigninActivity,
                MainActivity::class.java
            )
        ) else {
            NavHost(navCtrl, "launcher_screen") {
                composable("launcher_screen") {
                    LauncherScreen(navCtrl)
                }
                composable("signup_screen") {
                    SignupScreen()
                }
                composable("login_screen") {
                    LoginScreen()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun LoginPreview() {
        LoginScreen()
    }

    enum class TextfieldType {
        TEXT, EMAIL, PASSWORD
    }
}