package com.blogspot.e_kanivets.moneytracker.report.account;

import androidx.annotation.NonNull;

/**
 * Interface that represents a contract of access to accounts report data.
 * Created on 3/16/16.
 *
 * @author Evgenii Kanivets
 */
public interface IAccountsReport {
    /**
     * @return code of report currency
     */
    @NonNull
    String getCurrency();

    /**
     * @return total sum in given currency for given period
     */
    double getTotal();
}
