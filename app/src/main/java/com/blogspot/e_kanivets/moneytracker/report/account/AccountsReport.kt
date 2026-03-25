package com.blogspot.e_kanivets.moneytracker.report.account

import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider

/**
 * First [IAccountsReport] implementation.
 * Created on 3/16/16.
 *
 * @author Evgenii Kanivets
 */
class AccountsReport(
    override val currency: String,
    accountList: List<Account>,
    private val rateProvider: IExchangeRateProvider
) : IAccountsReport {

    override var total: Double = 0.0
        private set

    init {
        makeReport(accountList)
    }

    private fun makeReport(accountList: List<Account>) {
        total = 0.0

        for (account in accountList) {
            var convertedSum = account.fullSum

            if (currency != account.currency) {
                val exchangeRate = rateProvider.getRate(account)
                    ?: throw NullPointerException("No exchange rate found")
                convertedSum *= exchangeRate.amount
            }

            total += convertedSum
        }
    }
}
