package com.matrisciano.musixmatch.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.matrisciano.network.repository.UserRepository

class UserViewModel(private val repository: UserRepository): ViewModel() {

    fun getUser(userId: String) = repository.getUser(userId).asLiveData()
}
