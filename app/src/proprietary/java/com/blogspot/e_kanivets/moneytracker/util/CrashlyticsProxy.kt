package com.blogspot.e_kanivets.moneytracker.util

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class CrashlyticsProxy private constructor() {

    private var enabled: Boolean = false

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isEnabled(): Boolean = enabled

    fun logEvent(eventName: String?): Boolean {
        return if (enabled) {
            analytics?.logEvent(eventName, null)
            true
        } else {
            false
        }
    }

    fun logButton(buttonName: String?): Boolean {
        return if (enabled) {
            analytics?.logEvent(buttonName, null)
            true
        } else {
            false
        }
    }

    companion object {
        private var instance: CrashlyticsProxy? = null
        private var analytics: FirebaseAnalytics? = null

        @JvmStatic
        fun get(): CrashlyticsProxy {
            if (instance == null) instance = CrashlyticsProxy()
            return instance!!
        }

        @JvmStatic
        fun startCrashlytics(context: Context) {
            analytics = FirebaseAnalytics.getInstance(context)
        }
    }
}
