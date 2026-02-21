package com.blogspot.e_kanivets.moneytracker.util

import android.content.Context

class CrashlyticsProxy private constructor() {

    fun setEnabled(enabled: Boolean) {}

    fun isEnabled(): Boolean = false

    fun logEvent(eventName: String?): Boolean = false

    fun logButton(buttonName: String?): Boolean = false

    companion object {
        private var instance: CrashlyticsProxy? = null

        @JvmStatic
        fun get(): CrashlyticsProxy {
            if (instance == null) instance = CrashlyticsProxy()
            return instance!!
        }

        @JvmStatic
        fun startCrashlytics(context: Context) {}
    }
}
