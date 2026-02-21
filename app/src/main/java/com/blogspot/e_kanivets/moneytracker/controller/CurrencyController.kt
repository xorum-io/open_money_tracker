package com.blogspot.e_kanivets.moneytracker.controller

import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import java.util.Collections
import java.util.Currency
import java.util.Locale

/**
 * Controller class to encapsulate currency handling logic. Use embedded locales to obtain all currencies.
 * Not deal with [com.blogspot.e_kanivets.moneytracker.repo.base.IRepo] instances as others.
 * Created on 4/20/16.
 *
 * @author Evgenii Kanivets
 */
class CurrencyController(
    private val accountController: AccountController,
    private val preferenceController: PreferenceController
) {
    private val currencyList: List<String> = fetchCurrencies()

    fun readAll(): List<String> = currencyList

    fun readDefaultCurrency(): String {
        // First of all read from Prefs
        var currency = preferenceController.readDefaultCurrency()

        // If don't have default currency, try to use currency of default account
        if (currency == null) {
            currency = DbHelper.DEFAULT_ACCOUNT_CURRENCY
            val defaultAccount = accountController.readDefaultAccount()
            if (defaultAccount != null) currency = defaultAccount.currency
        }

        return currency
    }

    private fun fetchCurrencies(): List<String> {
        val toret = mutableSetOf<Currency>()
        val locs = Locale.getAvailableLocales()

        for (loc in locs) {
            try {
                toret.add(Currency.getInstance(loc))
            } catch (exc: Exception) {
                // Locale not found
            }
        }

        val currencySet = mutableListOf<String>()
        for (currency in toret) {
            currencySet.add(currency.currencyCode)
        }

        currencySet.add(DbHelper.DEFAULT_ACCOUNT_CURRENCY)
        currencySet.add("BYN") // New belorussian ruble

        val result = ArrayList(currencySet)
        Collections.sort(result)
        return result
    }
}
