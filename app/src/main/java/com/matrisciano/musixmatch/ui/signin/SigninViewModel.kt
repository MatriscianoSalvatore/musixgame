package com.matrisciano.musixmatch.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import com.matrisciano.network.repository.AuthRepository
import com.matrisciano.network.repository.UserRepository
import com.matrisciano.network.utils.Result

class SigninViewModel(private val userRepository: UserRepository, private val authRepository: AuthRepository) : ViewModel() {
    fun createUser(userID: String, email: String) = userRepository.createUser(userID, email).asLiveData()
    fun login(email: String, password: String): LiveData<Result<FirebaseUser?>> = authRepository.login(email, password).asLiveData()
    fun signup(name: String, email: String, password: String) = authRepository.signup(name, email, password).asLiveData()
    //fun getUserID() = authRepository.getUserID().asLiveData()
    //fun getUser() = authRepository.getUser().asLiveData()
}