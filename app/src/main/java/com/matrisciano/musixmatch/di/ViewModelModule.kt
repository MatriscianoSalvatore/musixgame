package com.matrisciano.musixmatch.di

import com.matrisciano.musixmatch.ui.main.home.HomeViewModel
import com.matrisciano.musixmatch.ui.main.profile.ProfileViewModel
import com.matrisciano.musixmatch.ui.main.leaderboard.LeaderboardViewModel
import com.matrisciano.musixmatch.ui.signin.SigninViewModel
import com.matrisciano.musixmatch.ui.whosings.WhoSingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SigninViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { LeaderboardViewModel(get()) }
    viewModel { WhoSingsViewModel(get()) }
}