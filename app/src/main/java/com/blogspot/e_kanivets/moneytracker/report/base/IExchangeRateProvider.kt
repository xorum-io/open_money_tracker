package com.blogspot.e_kanivets.moneytracker.report.base

import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.entity.data.Record

/**
 * Interface that represents a contract of access to currency exchange rate.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
interface IExchangeRateProvider {
    fun getRate(record: Record?): ExchangeRate?
    fun getRate(account: Account?): ExchangeRate?
}
