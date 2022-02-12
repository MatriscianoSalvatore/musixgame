package com.matrisciano.network.di

import com.google.firebase.firestore.FirebaseFirestore
import com.matrisciano.network.network.Network
import com.matrisciano.network.repository.*
import com.matrisciano.network.service.MusixmatchService
import com.matrisciano.network.utils.ResponseHandler
import org.koin.dsl.module

val networkModule = module {
    single { FirebaseFirestore.getInstance() }
    single { Network().createServiceApi(MusixmatchService::class) }
    single { ResponseHandler() }
    factory<MusicRepository> { MusicRepositoryImpl(get(), get()) }
    factory<UserRepository> { UserRepositoryImpl(get()) }
}
