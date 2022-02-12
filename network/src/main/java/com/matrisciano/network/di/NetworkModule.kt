package com.matrisciano.network.di

import com.google.firebase.firestore.FirebaseFirestore
import com.matrisciano.network.network.Network
import com.matrisciano.network.repository.MusixmatchRepository
import com.matrisciano.network.repository.MusixmatchRepositoryImpl
import com.matrisciano.network.repository.UserRepository
import com.matrisciano.network.repository.UserRepositoryImpl
import com.matrisciano.network.service.MusixmatchService
import com.matrisciano.network.utils.ResponseHandler
import org.koin.dsl.module

val networkModule = module {
    single { FirebaseFirestore.getInstance() }
    single { Network().createServiceApi(MusixmatchService::class) }
    single { ResponseHandler() }
    factory<MusixmatchRepository> { MusixmatchRepositoryImpl(get(), get()) }
    factory<UserRepository> { UserRepositoryImpl(get()) }
}
