package com.blogspot.e_kanivets.moneytracker.ui.presenter.base

import android.content.Context
import android.view.LayoutInflater
import com.blogspot.e_kanivets.moneytracker.R

abstract class BaseSummaryPresenter {
    protected lateinit var context: Context
    protected lateinit var layoutInflater: LayoutInflater

    protected fun createRatesNeededList(currency: String, ratesNeeded: List<String>): String {
        val sb = StringBuilder(context.getString(R.string.error_exchange_rates))
        for (str in ratesNeeded) {
            sb.append("\n").append(str).append(context.getString(R.string.arrow)).append(currency)
        }
        return sb.toString()
    }
}
