package com.blogspot.e_kanivets.moneytracker.report;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.report.account.AccountsReport;
import com.blogspot.e_kanivets.moneytracker.report.account.IAccountsReport;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;
import com.blogspot.e_kanivets.moneytracker.report.chart.MonthReport;
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport;
import com.blogspot.e_kanivets.moneytracker.report.record.RecordReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Util class to encapsulate {@link RecordReport} generation logic.
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
public class ReportMaker {
    private final ExchangeRateController rateController;

    public ReportMaker(ExchangeRateController exchangeRateController) {
        this.rateController = exchangeRateController;
    }

    @Nullable
    public IRecordReport getRecordReport(String currency, Period period, List<Record> recordList) {
        if (currencyNeeded(currency, recordList).size() != 0) return null;

        IExchangeRateProvider rateProvider = new ExchangeRateProvider(currency, rateController);
        return new RecordReport(currency, period, recordList, rateProvider);
    }

    @Nullable
    public IAccountsReport getAccountsReport(String currency, List<Account> accountList) {
        if (currencyNeededAccounts(currency, accountList).size() != 0) return null;

        IExchangeRateProvider rateProvider = new ExchangeRateProvider(currency, rateController);
        return new AccountsReport(currency, accountList, rateProvider);
    }

    @Nullable
    public IMonthReport getMonthReport(String currency, List<Record> recordList) {
        if (currencyNeeded(currency, recordList).size() != 0) return null;

        IExchangeRateProvider rateProvider = new ExchangeRateProvider(currency, rateController);
        return new MonthReport(recordList, currency, rateProvider);
    }

    @NonNull
    public List<String> currencyNeeded(String currency, List<Record> recordList) {
        Set<String> currencies = new TreeSet<>();

        for (Record record : recordList) {
            currencies.add(record.getCurrency());
        }

        currencies.remove(currency);

        for (ExchangeRate rate : rateController.readAll()) {
            if (rate.getToCurrency().equals(currency)) currencies.remove(rate.getFromCurrency());
        }

        return new ArrayList<>(currencies);
    }

    @NonNull
    public List<String> currencyNeededAccounts(String currency, List<Account> accountList) {
        Set<String> currencies = new TreeSet<>();

        for (Account account : accountList) {
            currencies.add(account.getCurrency());
        }

        currencies.remove(currency);

        for (ExchangeRate rate : rateController.readAll()) {
            if (rate.getToCurrency().equals(currency)) currencies.remove(rate.getFromCurrency());
        }

        return new ArrayList<>(currencies);
    }
}
