package com.matrisciano.musixmatch.ui.signin

import androidx.lifecycle.ViewModel
import com.matrisciano.network.repository.UserRepository

class SigninViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun createUser(userID: String, email: String) = userRepository.createUser(userID, email)
}