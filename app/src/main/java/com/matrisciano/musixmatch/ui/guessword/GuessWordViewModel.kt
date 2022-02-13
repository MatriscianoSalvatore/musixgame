package com.matrisciano.musixmatch.ui.guessword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.matrisciano.network.repository.UserRepository

class GuessWordViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun addPoints(userID: String, points: Long) = userRepository.addPoints(userID, points).asLiveData()
}