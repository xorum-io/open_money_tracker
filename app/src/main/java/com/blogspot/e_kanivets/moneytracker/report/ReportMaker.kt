package com.blogspot.e_kanivets.moneytracker.report

import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.account.AccountsReport
import com.blogspot.e_kanivets.moneytracker.report.account.IAccountsReport
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport
import com.blogspot.e_kanivets.moneytracker.report.chart.MonthReport
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport
import com.blogspot.e_kanivets.moneytracker.report.record.RecordReport
import java.util.TreeSet

/**
 * Util class to encapsulate [RecordReport] generation logic.
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
class ReportMaker(private val rateController: ExchangeRateController) {

    fun getRecordReport(currency: String, period: Period, recordList: List<Record>): IRecordReport? {
        if (currencyNeeded(currency, recordList).isNotEmpty()) return null

        val rateProvider = ExchangeRateProvider(currency, rateController)
        return RecordReport(currency, period, recordList, rateProvider)
    }

    fun getAccountsReport(currency: String, accountList: List<Account>): IAccountsReport? {
        if (currencyNeededAccounts(currency, accountList).isNotEmpty()) return null

        val rateProvider = ExchangeRateProvider(currency, rateController)
        return AccountsReport(currency, accountList, rateProvider)
    }

    fun getMonthReport(currency: String, recordList: List<Record>): IMonthReport? {
        if (currencyNeeded(currency, recordList).isNotEmpty()) return null

        val rateProvider = ExchangeRateProvider(currency, rateController)
        return MonthReport(recordList, currency, rateProvider)
    }

    fun currencyNeeded(currency: String, recordList: List<Record>): List<String> {
        val currencies = TreeSet<String>()

        for (record in recordList) {
            currencies.add(record.currency)
        }

        currencies.remove(currency)

        for (rate in rateController.readAll()) {
            if (rate.toCurrency == currency) currencies.remove(rate.fromCurrency)
        }

        return ArrayList(currencies)
    }

    fun currencyNeededAccounts(currency: String, accountList: List<Account>): List<String> {
        val currencies = TreeSet<String>()

        for (account in accountList) {
            currencies.add(account.currency)
        }

        currencies.remove(currency)

        for (rate in rateController.readAll()) {
            if (rate.toCurrency == currency) currencies.remove(rate.fromCurrency)
        }

        return ArrayList(currencies)
    }
}
