package com.blogspot.e_kanivets.moneytracker.report

import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider
import java.util.TreeMap

/**
 * First [IExchangeRateProvider] implementation.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
class ExchangeRateProvider(
    private val toCurrency: String,
    controller: ExchangeRateController
) : IExchangeRateProvider {

    private val rateMap: Map<String, ExchangeRate> = getRateMap(controller.readAll())

    override fun getRate(record: Record?): ExchangeRate? {
        if (record == null) return null
        return rateMap[record.currency]
    }

    override fun getRate(account: Account?): ExchangeRate? {
        if (account == null) return null
        return rateMap[account.currency]
    }

    private fun getRateMap(exchangeRateList: List<ExchangeRate>): Map<String, ExchangeRate> {
        val rateMap = TreeMap<String, ExchangeRate>()

        val sorted = exchangeRateList.sortedWith(Comparator { lhs, rhs ->
            if (lhs.createdAt < rhs.createdAt) -1
            else if (lhs.createdAt == rhs.createdAt) 0
            else 1
        })

        for (rate in sorted) {
            if (toCurrency != rate.toCurrency) continue
            rateMap[rate.fromCurrency ?: continue] = rate
        }

        return rateMap
    }
}
