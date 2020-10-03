package com.blogspot.e_kanivets.moneytracker.report.chart;

import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Interface that represents a contract of access to record report data grouped by month
 * for all records. All three methods must return list of the same size.
 * Created on 4/28/16.
 *
 * @author Evgenii Kanivets
 */
public interface IMonthReport extends Parcelable {
    /**
     * @return code of report currency
     */
    @NonNull String getCurrency();

    /**
     * @return list of month timestamps with not zero record count
     */
    @NonNull
    List<Long> getMonthList();

    /**
     * @return list of summary month incomes
     */
    @NonNull
    List<Double> getIncomeList();

    /**
     * @return list of summary month expenses
     */
    @NonNull
    List<Double> getExpenseList();
}
