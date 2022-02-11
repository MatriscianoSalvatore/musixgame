package com.matrisciano.network.di

import com.matrisciano.network.network.Network
import com.matrisciano.network.service.ApiService
import org.koin.dsl.module

val networkModule = module {
    single { Network().createServiceApi(ApiService::class) }
}