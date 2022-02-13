package com.matrisciano.musixmatch.di

import com.matrisciano.musixmatch.ui.signin.SigninViewModel
import com.matrisciano.musixmatch.ui.main.home.HomeViewModel
import com.matrisciano.musixmatch.ui.main.profile.ProfileViewModel
import com.matrisciano.musixmatch.ui.main.leaderboard.LeaderboardViewModel
import com.matrisciano.musixmatch.ui.guessword.GuessWordViewModel
import com.matrisciano.musixmatch.ui.whosings.WhoSingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SigninViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { LeaderboardViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { GuessWordViewModel(get()) }
    viewModel { WhoSingsViewModel(get(), get()) }
}