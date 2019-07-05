package com.chyrta.mimi

import android.app.Application
import com.chyrta.mimi.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class MimiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()

        startKoin {
            androidLogger()
            androidContext(this@MimiApplication)
            modules(listOf(repositoryModule, networkModule, schedulerProviders, artistsModule, artistModule, playerModule))
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
