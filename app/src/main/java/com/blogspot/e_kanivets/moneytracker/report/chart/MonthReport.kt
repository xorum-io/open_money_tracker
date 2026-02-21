package com.blogspot.e_kanivets.moneytracker.report.chart

import android.os.Parcel
import android.os.Parcelable
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider
import java.util.Calendar
import java.util.Date
import java.util.TreeMap

/**
 * First [IMonthReport] implementation.
 * Created on 4/28/16.
 *
 * @author Evgenii Kanivets
 */
class MonthReport : IMonthReport {

    override val currency: String
    private val nodeList: List<MonthNode>

    override val monthList: List<Long> get() = nodeList.map { it.timestamp }
    override val incomeList: List<Double> get() = nodeList.map { it.totalIncome }
    override val expenseList: List<Double> get() = nodeList.map { it.totalExpense }

    constructor(recordList: List<Record>, currency: String, rateProvider: IExchangeRateProvider) {
        this.currency = currency
        this.nodeList = generateReport(recordList, rateProvider)
    }

    protected constructor(parcel: Parcel) {
        currency = parcel.readString()!!
        nodeList = parcel.createTypedArrayList(MonthNode.CREATOR)!!
    }

    private fun generateReport(
        recordList: List<Record>,
        rateProvider: IExchangeRateProvider
    ): List<MonthNode> {
        val monthMap = TreeMap<Long, MonthNode>()

        for (record in recordList) {
            val timestamp = getMonthTimestamp(record.time)

            if (monthMap[timestamp] == null) monthMap[timestamp] = MonthNode(timestamp)
            val node = monthMap[timestamp]!!

            var convertedPrice = record.fullPrice
            if (currency != record.currency) {
                val exchangeRate = rateProvider.getRate(record)
                    ?: throw NullPointerException("No exchange rate found")
                convertedPrice *= exchangeRate.amount
            }

            when (record.type) {
                Record.TYPE_INCOME -> node.addIncome(convertedPrice)
                Record.TYPE_EXPENSE -> node.addExpense(convertedPrice)
            }
        }

        return monthMap.values.toList()
    }

    private fun getMonthTimestamp(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.time = Date(timestamp)

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(currency)
        dest.writeTypedList(nodeList)
    }

    private class MonthNode : Parcelable {
        val timestamp: Long
        var totalIncome: Double = 0.0
        var totalExpense: Double = 0.0

        constructor(timestamp: Long) {
            this.timestamp = timestamp
        }

        constructor(parcel: Parcel) {
            timestamp = parcel.readLong()
            totalIncome = parcel.readDouble()
            totalExpense = parcel.readDouble()
        }

        fun addIncome(income: Double) {
            totalIncome += income
        }

        fun addExpense(expense: Double) {
            totalExpense += expense
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeLong(timestamp)
            dest.writeDouble(totalIncome)
            dest.writeDouble(totalExpense)
        }

        companion object CREATOR : Parcelable.Creator<MonthNode> {
            override fun createFromParcel(parcel: Parcel): MonthNode = MonthNode(parcel)
            override fun newArray(size: Int): Array<MonthNode?> = arrayOfNulls(size)
        }
    }

    companion object CREATOR : Parcelable.Creator<MonthReport> {
        override fun createFromParcel(parcel: Parcel): MonthReport = MonthReport(parcel)
        override fun newArray(size: Int): Array<MonthReport?> = arrayOfNulls(size)
    }
}
