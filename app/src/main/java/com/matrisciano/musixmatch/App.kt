package com.matrisciano.musixmatch

import android.app.Application
import com.matrisciano.network.di.networkModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            //androidLogger()
            modules(listOf(networkModule))
        }
    }
}