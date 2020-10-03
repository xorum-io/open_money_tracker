package com.blogspot.e_kanivets.moneytracker.report.account;

import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;

import java.util.List;

/**
 * First {@link IAccountsReport} implementation.
 * Created on 3/16/16.
 *
 * @author Evgenii Kanivets
 */
public class AccountsReport implements IAccountsReport {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountsReport";

    private String currency;
    private IExchangeRateProvider rateProvider;

    private double total;

    public AccountsReport(String currency, List<Account> accountList, IExchangeRateProvider rateProvider) {
        if (currency == null || rateProvider == null)
            throw new NullPointerException("Params can't be null");

        this.currency = currency;
        this.rateProvider = rateProvider;

        makeReport(accountList);
    }

    @NonNull
    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public double getTotal() {
        return total;
    }

    private void makeReport(List<Account> accountList) {
        total = 0;

        for (Account account : accountList) {
            double convertedSum = account.getFullSum();

            if (!currency.equals(account.getCurrency())) {
                ExchangeRate exchangeRate = rateProvider.getRate(account);
                if (exchangeRate == null) throw new NullPointerException("No exchange rate found");
                convertedSum *= exchangeRate.getAmount();
            }

            total += convertedSum;
        }
    }
}
