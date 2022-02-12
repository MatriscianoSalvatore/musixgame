package com.matrisciano.musixmatch.di

import com.matrisciano.musixmatch.ui.main.MainViewModel
import com.matrisciano.musixmatch.ui.main.home.HomeViewModel
import com.matrisciano.musixmatch.ui.main.profile.ProfileViewModel
import com.matrisciano.musixmatch.ui.guessword.GuessWordViewModel
import com.matrisciano.musixmatch.ui.main.profile.UserViewModel
import com.matrisciano.musixmatch.ui.whosings.WhoSingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { GuessWordViewModel(get()) }
    viewModel { WhoSingsViewModel(get(), get()) }
    viewModel { UserViewModel(get()) }
}
