package com.blogspot.e_kanivets.moneytracker.controller

import com.blogspot.e_kanivets.moneytracker.entity.Period
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.Calendar

class PeriodControllerTest {
    private lateinit var periodController: PeriodController

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val prefsMock = Mockito.mock(PreferenceController::class.java)
        periodController = PeriodController(prefsMock)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun testDayPeriod() {
        val period = periodController.dayPeriod()
        assertEquals(Period.TYPE_DAY, period.type)
        validateBounds(period)
    }

    @Test
    @Throws(Exception::class)
    fun testWeekPeriod() {
        val period = periodController.weekPeriod()
        assertEquals(Period.TYPE_WEEK, period.type)
        validateBounds(period)
    }

    @Test
    @Throws(Exception::class)
    fun testMonthPeriod() {
        val period = periodController.monthPeriod()
        assertEquals(Period.TYPE_MONTH, period.type)
        validateBounds(period)
    }

    @Test
    @Throws(Exception::class)
    fun testYearPeriod() {
        val period = periodController.yearPeriod()
        assertEquals(Period.TYPE_YEAR, period.type)
        validateBounds(period)
    }

    private fun validateBounds(period: Period): Boolean {
        val first = Calendar.getInstance()
        first.time = period.first

        assertEquals(0, first.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, first.get(Calendar.MINUTE))
        assertEquals(0, first.get(Calendar.SECOND))
        assertEquals(0, first.get(Calendar.MILLISECOND))

        val last = Calendar.getInstance()
        last.time = period.last

        assertEquals(23, last.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, last.get(Calendar.MINUTE))
        assertEquals(59, last.get(Calendar.SECOND))
        assertEquals(999, last.get(Calendar.MILLISECOND))

        return true
    }
}
