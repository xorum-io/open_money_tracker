package com.blogspot.e_kanivets.moneytracker

import android.app.Application
import com.blogspot.e_kanivets.moneytracker.di.appModule
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import timber.log.Timber

class MtApp : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
        startKoin {
            androidContext(this@MtApp)
            modules(appModule)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            CrashlyticsProxy.instance.setEnabled(false)
        } else {
            Timber.plant(ReleaseTree())
            CrashlyticsProxy.startCrashlytics(this)
            CrashlyticsProxy.instance.setEnabled(true)
        }
    }

    fun restartKoin() {
        stopKoin()
        startKoin {
            androidContext(this@MtApp)
            modules(appModule)
        }
    }

    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Do nothing for now
        }
    }

    companion object {

        lateinit var instance: MtApp
            private set
    }
}
