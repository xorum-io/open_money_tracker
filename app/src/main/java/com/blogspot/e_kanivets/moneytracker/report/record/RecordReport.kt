package com.blogspot.e_kanivets.moneytracker.report.record

import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider
import com.blogspot.e_kanivets.moneytracker.report.record.model.CategoryRecord
import com.blogspot.e_kanivets.moneytracker.report.record.model.SummaryRecord
import java.util.TreeMap

/**
 * First [IRecordReport] implementation.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
class RecordReport(
    override val currency: String,
    override val period: Period,
    recordList: List<Record>,
    private val rateProvider: IExchangeRateProvider
) : IRecordReport {

    override var totalIncome: Double = 0.0
        private set
    override var totalExpense: Double = 0.0
        private set
    override val total: Double get() = totalExpense + totalIncome
    override val summary: List<CategoryRecord> get() = categoryRecordList

    private val categoryRecordList: MutableList<CategoryRecord> = mutableListOf()

    init {
        makeReport(recordList)
    }

    private fun makeReport(recordList: List<Record>) {
        totalIncome = 0.0
        totalExpense = 0.0
        categoryRecordList.clear()

        val convertedRecordList = convertRecordList(recordList)

        val categorySortedMap = TreeMap<String, MutableList<Record>>()

        for (record in convertedRecordList) {
            when (record.type) {
                Record.TYPE_INCOME -> totalIncome += record.fullPrice
                Record.TYPE_EXPENSE -> totalExpense -= record.fullPrice
            }

            val categoryName = record.category?.name ?: continue

            if (!categorySortedMap.containsKey(categoryName))
                categorySortedMap[categoryName] = mutableListOf()
            categorySortedMap[categoryName]!!.add(record)
        }

        for (category in categorySortedMap.keys) {
            categoryRecordList.add(createCategoryRecord(category, categorySortedMap[category]!!))
        }

        categoryRecordList.sortWith(Comparator { lhs, rhs -> compareDoubles(lhs.amount, rhs.amount) })
    }

    private fun convertRecordList(recordList: List<Record>): List<Record> {
        val convertedRecordList = mutableListOf<Record>()

        for (record in recordList) {
            var convertedPrice = record.fullPrice

            if (currency != record.currency) {
                val exchangeRate = rateProvider.getRate(record)
                    ?: throw NullPointerException("No exchange rate found")
                convertedPrice *= exchangeRate.amount
            }

            val intConvertedPrice = convertedPrice.toInt()
            // Strange calculation because of double type precision issue
            val decConvertedPrice = Math.round(convertedPrice * 100 - intConvertedPrice * 100).toInt()

            val convertedRecord = Record(
                record.id, record.time, record.type,
                record.title, record.category, record.notes, intConvertedPrice.toLong(),
                record.account, currency, decConvertedPrice.toLong()
            )

            convertedRecordList.add(convertedRecord)
        }

        return convertedRecordList
    }

    private fun createCategoryRecord(category: String, recordList: List<Record>): CategoryRecord {
        val titleSortedMap = TreeMap<String, MutableList<Record>>()

        var amount = 0.0

        for (record in recordList) {
            amount += getAmount(record)

            val title = record.title ?: ""

            if (!titleSortedMap.containsKey(title))
                titleSortedMap[title] = mutableListOf()
            titleSortedMap[title]!!.add(record)
        }

        val categoryRecord = CategoryRecord(category, currency, amount)

        for (title in titleSortedMap.keys) {
            categoryRecord.add(createSummaryRecord(title, titleSortedMap[title]!!))
        }

        categoryRecord.summaryRecordList.sortWith(
            Comparator { lhs, rhs -> compareDoubles(lhs.amount, rhs.amount) }
        )

        return categoryRecord
    }

    private fun createSummaryRecord(title: String, recordList: List<Record>): SummaryRecord {
        var amount = 0.0

        for (record in recordList) {
            amount += getAmount(record)
        }

        return SummaryRecord(title, currency, amount, recordList.size)
    }

    private fun getAmount(record: Record): Double {
        return when (record.type) {
            Record.TYPE_INCOME -> record.fullPrice
            Record.TYPE_EXPENSE -> -record.fullPrice
            else -> 0.0
        }
    }

    private fun compareDoubles(lhs: Double, rhs: Double): Int {
        return if (lhs > 0 && rhs < 0) -1
        else if (lhs < 0 && rhs > 0) 1
        else -1 * Math.abs(lhs).compareTo(Math.abs(rhs))
    }
}
