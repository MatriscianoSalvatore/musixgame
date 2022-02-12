package com.matrisciano.musixmatch.ui.main.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.matrisciano.network.repository.UserRepository

class LeaderboardViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUsers() = userRepository.getUsers().asLiveData()
}