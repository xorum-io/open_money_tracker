package com.blogspot.e_kanivets.moneytracker.report.chart

import android.os.Parcelable

/**
 * Interface that represents a contract of access to record report data grouped by month
 * for all records. All three properties must return list of the same size.
 * Created on 4/28/16.
 *
 * @author Evgenii Kanivets
 */
interface IMonthReport : Parcelable {
    /**
     * @return code of report currency
     */
    val currency: String

    /**
     * @return list of month timestamps with not zero record count
     */
    val monthList: List<Long>

    /**
     * @return list of summary month incomes
     */
    val incomeList: List<Double>

    /**
     * @return list of summary month expenses
     */
    val expenseList: List<Double>
}
