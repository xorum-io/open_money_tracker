package com.blogspot.e_kanivets.moneytracker

import android.app.Application
import com.blogspot.e_kanivets.moneytracker.di.AppComponent
import com.blogspot.e_kanivets.moneytracker.di.DaggerAppComponent
import com.blogspot.e_kanivets.moneytracker.di.module.ControllerModule
import com.blogspot.e_kanivets.moneytracker.di.module.repo.CachedRepoModule
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import timber.log.Timber

/**
 * Custom application implementation.
 * Created on 29/08/14.
 * @author Evgenii Kanivets
 */
class MtApp : Application() {
    companion object {
        lateinit var mtApp: MtApp
            private set
        fun get(): MtApp = mtApp
    }

    var component: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        mtApp = this
        buildAppComponent()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            CrashlyticsProxy.get().setEnabled(false)
        } else {
            Timber.plant(ReleaseTree())
            CrashlyticsProxy.startCrashlytics(this)
            CrashlyticsProxy.get().setEnabled(true)
        }
    }

    fun getAppComponent(): AppComponent? = component

    fun buildAppComponent() {
        component = buildComponent()
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .cachedRepoModule(CachedRepoModule(get()))
            .controllerModule(ControllerModule(get()))
            .build()
    }

    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // Do nothing for now
        }
    }
} 