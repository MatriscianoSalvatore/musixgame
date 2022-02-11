package com.matrisciano.musixmatch.di

import com.matrisciano.musixmatch.ui.main.home.HomeViewModel
import com.matrisciano.musixmatch.ui.whosings.WhoSingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { WhoSingsViewModel(get()) }
}