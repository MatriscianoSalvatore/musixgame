package com.matrisciano.musixmatch.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.matrisciano.network.repository.AuthRepository
import com.matrisciano.network.repository.UserRepository

class ProfileViewModel(private val userRepository: UserRepository, val authRepository: AuthRepository) : ViewModel() {
    fun getUser(userID: String) = userRepository.getUser(userID).asLiveData()
    fun logout() = authRepository.logout().asLiveData()
}