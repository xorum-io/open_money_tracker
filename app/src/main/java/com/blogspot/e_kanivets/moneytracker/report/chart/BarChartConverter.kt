package com.blogspot.e_kanivets.moneytracker.report.chart

import android.annotation.SuppressLint
import android.content.Context
import com.blogspot.e_kanivets.moneytracker.R
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Util class to convert [IMonthReport] to [com.github.mikephil.charting.charts.BarChart]
 * input data.
 * Created on 4/27/16.
 *
 * @author Evgenii Kanivets
 */
class BarChartConverter(context: Context, private val report: IMonthReport) {

    private val green: Int
    private val red: Int
    private val incomesTitle: String
    private val expensesTitle: String

    init {
        @Suppress("DEPRECATION")
        green = context.resources.getColor(R.color.green_light)
        @Suppress("DEPRECATION")
        red = context.resources.getColor(R.color.red_light)
        incomesTitle = context.getString(R.string.incomes)
        expensesTitle = context.getString(R.string.expenses)
    }

    val xAxisValueList: List<String> get() {
        val valueList = mutableListOf<String>()

        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("MMM yy")
        for (timestamp in report.monthList) {
            valueList.add(sdf.format(Date(timestamp)))
        }

        return valueList
    }

    val barDataSetList: List<IBarDataSet> get() {
        val incomeList = mutableListOf<BarEntry>()
        for (i in 0 until report.incomeList.size) {
            incomeList.add(BarEntry(report.incomeList[i].toFloat(), i))
        }

        val incomeDataSet = BarDataSet(incomeList, incomesTitle)
        incomeDataSet.color = green

        val expenseList = mutableListOf<BarEntry>()
        for (i in 0 until report.expenseList.size) {
            expenseList.add(BarEntry(report.expenseList[i].toFloat(), i))
        }

        val dataSet2 = BarDataSet(expenseList, expensesTitle)
        dataSet2.color = red

        return listOf(incomeDataSet, dataSet2)
    }
}
