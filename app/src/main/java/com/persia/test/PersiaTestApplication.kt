package com.persia.test

import android.app.Application
import timber.log.Timber

class PersiaTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}