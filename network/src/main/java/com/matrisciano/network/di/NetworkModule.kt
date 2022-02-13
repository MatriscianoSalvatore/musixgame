package com.matrisciano.network.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.matrisciano.network.network.Network
import com.matrisciano.network.repository.*
import com.matrisciano.network.service.MusixmatchService
import com.matrisciano.network.utils.ResponseHandler
import org.koin.dsl.module

val networkModule = module {
    single { FirebaseFirestore.getInstance() }
    single { Firebase.auth }
    single { Network().createServiceApi(MusixmatchService::class) }
    single { ResponseHandler() }
    factory<MusicRepository> { MusicRepositoryImpl(get(), get()) }
    factory<UserRepository> { UserRepositoryImpl(get()) }
    factory<AuthRepository> { AuthRepositoryImpl(get()) }
}