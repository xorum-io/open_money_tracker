package com.blogspot.e_kanivets.moneytracker.util

import android.content.Context

class CrashlyticsProxy private constructor() {

    fun setEnabled(enabled: Boolean) {}

    fun isEnabled(): Boolean = false

    fun logEvent(eventName: String?): Boolean = false

    fun logButton(buttonName: String?): Boolean = false

    companion object {

        val instance = CrashlyticsProxy()

        fun startCrashlytics(context: Context) {}
    }
}
