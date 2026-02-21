package com.blogspot.e_kanivets.moneytracker.report.account

/**
 * Interface that represents a contract of access to accounts report data.
 * Created on 3/16/16.
 *
 * @author Evgenii Kanivets
 */
interface IAccountsReport {
    /**
     * @return code of report currency
     */
    val currency: String

    /**
     * @return total sum in given currency for given period
     */
    val total: Double
}
