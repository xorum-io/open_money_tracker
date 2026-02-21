package com.blogspot.e_kanivets.moneytracker.report.record

import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.report.record.model.CategoryRecord

/**
 * Interface that represents a contract of access to report data.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
interface IRecordReport {
    /**
     * @return code of report currency
     */
    val currency: String

    /**
     * @return period of report
     */
    val period: Period

    /**
     * @return total sum in given currency for given period
     */
    val total: Double

    /**
     * @return total of all incomes for given period
     */
    val totalIncome: Double

    /**
     * @return total of all expenses for given period
     */
    val totalExpense: Double

    /**
     * @return summary list
     */
    val summary: List<CategoryRecord>
}
