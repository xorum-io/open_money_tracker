package com.blogspot.e_kanivets.moneytracker.report

import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport
import com.blogspot.e_kanivets.moneytracker.report.record.RecordReport
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

class ReportTest {
    private lateinit var currency: String
    private lateinit var rateProvider: IExchangeRateProvider

    @Before
    @Throws(Exception::class)
    fun setUp() {
        currency = "UAH"
        rateProvider = TestProvider()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun testForNulls() {
        var report: IRecordReport?

        val period = Period(Date(1), Date(), Period.TYPE_CUSTOM)
        val recordList = emptyList<Record>()

        try {
            @Suppress("UNCHECKED_CAST")
            report = RecordReport(null as String, period, recordList, rateProvider)
        } catch (e: NullPointerException) {
            report = null
        }
        assertNull(report)

        try {
            @Suppress("UNCHECKED_CAST")
            report = RecordReport(currency, null as Period, recordList, rateProvider)
        } catch (e: NullPointerException) {
            report = null
        }
        assertNull(report)

        try {
            @Suppress("UNCHECKED_CAST")
            report = RecordReport(currency, period, null as List<Record>, rateProvider)
        } catch (e: NullPointerException) {
            report = null
        }
        assertNull(report)

        try {
            @Suppress("UNCHECKED_CAST")
            report = RecordReport(currency, period, recordList, null as IExchangeRateProvider)
        } catch (e: NullPointerException) {
            report = null
        }
        assertNull(report)

        try {
            @Suppress("UNCHECKED_CAST")
            report = RecordReport(null as String, null as Period, null as List<Record>, null as IExchangeRateProvider)
        } catch (e: NullPointerException) {
            report = null
        }
        assertNull(report)

        try {
            report = RecordReport(currency, period, recordList, rateProvider)
        } catch (e: NullPointerException) {
            report = null
        }
        assertNotNull(report)
    }

    @Test
    @Throws(Exception::class)
    fun testGetCurrency() {
        val period = Period(Date(1), Date(), Period.TYPE_CUSTOM)
        val recordList = emptyList<Record>()

        var report: IRecordReport = RecordReport(currency, period, recordList, rateProvider)
        assertEquals(currency, report.currency)

        currency = "KHI"
        report = RecordReport(currency, period, recordList, rateProvider)
        assertEquals(currency, report.currency)
    }

    @Test
    @Throws(Exception::class)
    fun testGetPeriod() {
        var period = Period(Date(1), Date(), Period.TYPE_CUSTOM)
        val recordList = emptyList<Record>()

        var report: IRecordReport = RecordReport(currency, period, recordList, rateProvider)
        assertEquals(period, report.period)

        period = Period(Date(3), Date(100), Period.TYPE_CUSTOM)
        report = RecordReport(currency, period, recordList, rateProvider)
        assertEquals(period, report.period)
    }

    @Test
    @Throws(Exception::class)
    fun testGetTotal() {
        val period = Period(Date(1), Date(), Period.TYPE_CUSTOM)
        val recordList = getRecordList()

        val report: IRecordReport = RecordReport(currency, period, recordList, rateProvider)

        val expectedTotal = 10 * 4 - 2 + 5 - 10 * 4.0
        assertEquals(expectedTotal, report.total, 0.0000000001)
    }

    @Test
    @Throws(Exception::class)
    fun testGetTotalIncome() {
        val period = Period(Date(1), Date(), Period.TYPE_CUSTOM)
        val recordList = getRecordList()

        val report: IRecordReport = RecordReport(currency, period, recordList, rateProvider)

        val expectedTotal = 10 * 4 + 5.0
        assertEquals(expectedTotal, report.totalIncome, 0.0000000001)
    }

    @Test
    @Throws(Exception::class)
    fun testGetTotalExpense() {
        val period = Period(Date(1), Date(), Period.TYPE_CUSTOM)
        val recordList = getRecordList()

        val report: IRecordReport = RecordReport(currency, period, recordList, rateProvider)

        val expectedTotal = -2 - 10 * 4.0
        assertEquals(expectedTotal, report.totalExpense, 0.0000000001)
    }

    @Test
    @Throws(Exception::class)
    fun testGetSummary() {
        val period = Period(Date(1), Date(), Period.TYPE_CUSTOM)
        val recordList = mutableListOf<Record>()

        val category = Category(1L, "category")
        val account1 = Account(1L, "account1", 100L, "UAH", 0L, 0.0, false, 0)
        val account2 = Account(2L, "account2", 100L, "USD", 0L, 0.0, false, 0)

        recordList.add(Record(1L, 0L, Record.TYPE_INCOME, "1", category, null, 10.0, account2, "USD"))
        recordList.add(Record(2L, 1L, Record.TYPE_EXPENSE, "1", category, null, 2.0, account1, "UAH"))
        recordList.add(Record(3L, 2L, Record.TYPE_INCOME, "3", category, null, 5.0, account1, "UAH"))
        recordList.add(Record(4L, 3L, Record.TYPE_EXPENSE, "4", category, null, 10.0, account2, "USD"))

        val report: IRecordReport = RecordReport(currency, period, recordList, rateProvider)

        val summary = report.summary
        assertEquals(1, summary.size)

        val categoryRecord = summary[0]
        assertEquals("category", categoryRecord.title)
        assertEquals(currency, categoryRecord.currency)
        assertEquals(10 * 4 - 2 + 5 - 10 * 4.0, categoryRecord.amount, 0.0000000001)

        assertEquals(3, categoryRecord.summaryRecordList.size)

        val amounts = categoryRecord.summaryRecordList.map { it.amount }.toSet()
        assertTrue(amounts.contains(38.0))
        assertTrue(amounts.contains(5.0))
        assertTrue(amounts.contains(-40.0))
    }

    private fun getRecordList(): List<Record> {
        val recordList = mutableListOf<Record>()

        val category = Category(1L, "category")
        val account1 = Account(1L, "account1", 100L, "UAH", 0L, 0.0, false, 0)
        val account2 = Account(2L, "account2", 100L, "USD", 0L, 0.0, false, 0)

        recordList.add(Record(1L, 0L, Record.TYPE_INCOME, "1", category, null, 10.0, account2, "USD"))
        recordList.add(Record(2L, 1L, Record.TYPE_EXPENSE, "1", category, null, 2.0, account1, "UAH"))
        recordList.add(Record(3L, 2L, Record.TYPE_INCOME, "3", category, null, 5.0, account1, "UAH"))
        recordList.add(Record(4L, 3L, Record.TYPE_EXPENSE, "4", category, null, 10.0, account2, "USD"))

        return recordList
    }

    private class TestProvider : IExchangeRateProvider {
        override fun getRate(record: Record?): ExchangeRate? {
            if (record == null) return null
            return when (record.currency) {
                "USD" -> ExchangeRate(1L, "USD", "UAH", 4.0)
                "AFN" -> ExchangeRate(0L, "AFN", "UAH", 3.0)
                else -> null
            }
        }

        override fun getRate(account: Account?): ExchangeRate? = null
    }
}
