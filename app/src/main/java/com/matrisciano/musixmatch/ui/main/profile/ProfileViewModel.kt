package com.matrisciano.musixmatch.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.matrisciano.network.repository.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUser(userID: String) = userRepository.getUser(userID).asLiveData()
}