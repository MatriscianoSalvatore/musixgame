package com.matrisciano.network.di

import com.matrisciano.network.network.Network
import com.matrisciano.network.repository.MusixmatchRepository
import com.matrisciano.network.repository.MusixmatchRepositoryImpl
import com.matrisciano.network.service.MusixmatchService
import com.matrisciano.network.utils.ResponseHandler
import org.koin.dsl.module

val networkModule = module {
    single { Network().createServiceApi(MusixmatchService::class) }
    single { ResponseHandler() }
    factory<MusixmatchRepository> { MusixmatchRepositoryImpl(get(), get()) }
}