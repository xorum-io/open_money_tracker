package com.blogspot.e_kanivets.moneytracker.controller;

import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Controller class to encapsulate currency handling logic. Use embedded locales to obtain all currencies.
 * Not deal with {@link com.blogspot.e_kanivets.moneytracker.repo.base.IRepo} instances as others.
 * Created on 4/20/16.
 *
 * @author Evgenii Kanivets
 */
public class CurrencyController {
    private AccountController accountController;
    private PreferenceController preferenceController;

    @NonNull private List<String> currencyList;

    public CurrencyController(AccountController accountController, PreferenceController preferenceController) {
        this.accountController = accountController;
        this.preferenceController = preferenceController;
        currencyList = fetchCurrencies();
    }

    @NonNull public List<String> readAll() {
        return currencyList;
    }

    @NonNull public String readDefaultCurrency() {
        // First of all read from Prefs
        String currency = preferenceController.readDefaultCurrency();

        // If don't have default currency, try to use currency of default account
        if (currency == null) {
            currency = DbHelper.DEFAULT_ACCOUNT_CURRENCY;
            Account defaultAccount = accountController.readDefaultAccount();
            if (defaultAccount != null) currency = defaultAccount.getCurrency();
        }

        return currency;
    }

    @NonNull private List<String> fetchCurrencies() {
        Set<Currency> toret = new HashSet<>();
        Locale[] locs = Locale.getAvailableLocales();

        for (Locale loc : locs) {
            try {
                toret.add(Currency.getInstance(loc));
            } catch (Exception exc) {
                // Locale not found
            }
        }

        List<String> currencySet = new ArrayList<>();
        for (Currency currency : toret) {
            currencySet.add(currency.getCurrencyCode());
        }

        currencySet.add(DbHelper.DEFAULT_ACCOUNT_CURRENCY);
        currencySet.add("BYN"); // New belorussian ruble

        currencyList = new ArrayList<>(currencySet);

        Collections.sort(currencyList);

        return currencyList;
    }
}
