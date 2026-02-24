package com.blogspot.e_kanivets.moneytracker.report

import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ExchangeRateProviderTest {
    private lateinit var rateController: ExchangeRateController

    @Before
    @Throws(Exception::class)
    fun setUp() {
        rateController = ExchangeRateController(TestRepo())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun testGetRate() {
        var provider: IExchangeRateProvider?

        try {
            @Suppress("UNCHECKED_CAST")
            provider = ExchangeRateProvider(null as String, rateController)
        } catch (e: NullPointerException) {
            provider = null
        }
        assertNull(provider)

        try {
            @Suppress("UNCHECKED_CAST")
            provider = ExchangeRateProvider("", null as ExchangeRateController)
        } catch (e: NullPointerException) {
            provider = null
        }
        assertNull(provider)

        try {
            @Suppress("UNCHECKED_CAST")
            provider = ExchangeRateProvider(null as String, null as ExchangeRateController)
        } catch (e: NullPointerException) {
            provider = null
        }
        assertNull(provider)

        provider = ExchangeRateProvider("USD", rateController)

        assertEquals(ExchangeRate(1L, "UAH", "USD", 4.0),
            provider.getRate(Record(1L, 0L, 0, "", 1L, null, 0L, 0L, "UAH", 0L)))

        assertEquals(ExchangeRate(0L, "AFN", "USD", 3.0),
            provider.getRate(Record(1L, 0L, 0, "", 1L, null, 0L, 0L, "AFN", 0L)))

        assertNull(provider.getRate(Record(1L, 0L, 0, "", 1L, null, 0L, 0L, "SMTH", 0L)))
    }

    private class TestRepo : IRepo<ExchangeRate> {
        override fun create(instance: ExchangeRate?): ExchangeRate? = null
        override fun read(id: Long): ExchangeRate? = null
        override fun update(instance: ExchangeRate?): ExchangeRate? = null
        override fun delete(instance: ExchangeRate?): Boolean = false

        override fun readAll(): List<ExchangeRate> = listOf(
            ExchangeRate(1L, "UAH", "USD", 4.0),
            ExchangeRate(0L, "UAH", "USD", 2.0),
            ExchangeRate(0L, "AFN", "USD", 3.0),
            ExchangeRate(0L, "USD", "EUR", 20.0)
        )

        override fun readWithCondition(condition: String?, args: Array<String?>?): List<ExchangeRate> = emptyList()
    }
}
