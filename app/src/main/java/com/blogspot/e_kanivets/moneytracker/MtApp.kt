package com.blogspot.e_kanivets.moneytracker

import android.app.Application
import com.blogspot.e_kanivets.moneytracker.di.AppComponent
import com.blogspot.e_kanivets.moneytracker.di.DaggerAppComponent
import com.blogspot.e_kanivets.moneytracker.di.appModule
import com.blogspot.e_kanivets.moneytracker.di.module.ControllerModule
import com.blogspot.e_kanivets.moneytracker.di.module.repo.CachedRepoModule
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import timber.log.Timber

class MtApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        instance = this
        buildAppComponent()
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

    fun buildAppComponent() {
        appComponent = buildComponent()
    }

    fun restartKoin() {
        stopKoin()
        startKoin {
            androidContext(this@MtApp)
            modules(appModule)
        }
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .cachedRepoModule(CachedRepoModule(instance))
            .controllerModule(ControllerModule(instance))
            .build()
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
