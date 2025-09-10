package com.blogspot.e_kanivets.moneytracker.controller

import com.blogspot.e_kanivets.moneytracker.entity.Period
import java.util.Calendar

class PeriodController(private val preferenceController: PreferenceController) {

    fun readLastUsedPeriod(): Period {
        val first = preferenceController.readFirstTs()
        val last = preferenceController.readLastTs()
        val type = preferenceController.readPeriodType()
        return if (first == -1L || last == -1L || type == null) {
            weekPeriod()
        } else {
            when (type) {
                Period.TYPE_DAY -> dayPeriod()
                Period.TYPE_WEEK -> weekPeriod()
                Period.TYPE_MONTH -> monthPeriod()
                Period.TYPE_YEAR -> yearPeriod()
                Period.TYPE_ALL_TIME -> allTimePeriod()
                Period.TYPE_CUSTOM -> customPeriod(first, last)
                else -> weekPeriod()
            }
        }
    }

    fun writeLastUsedPeriod(period: Period) {
        preferenceController.writeFirstTs(period.first.time)
        preferenceController.writeLastTs(period.last.time)
        preferenceController.writePeriodType(period.type)
    }

    fun customPeriod(firstTs: Long, lastTs: Long): Period {
        val cal = Calendar.getInstance()
        cal.timeInMillis = firstTs
        val first = cal.time
        cal.timeInMillis = lastTs
        val last = cal.time
        return Period(first, last, Period.TYPE_CUSTOM)
    }

    fun dayPeriod(): Period {
        val cal = Calendar.getInstance()
        setDayStart(cal)
        val first = cal.time
        setDayEnd(cal)
        val last = cal.time
        return Period(first, last, Period.TYPE_DAY)
    }

    fun weekPeriod(): Period {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        setDayStart(cal)
        val first = cal.time
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek + 6)
        setDayEnd(cal)
        val last = cal.time
        return Period(first, last, Period.TYPE_WEEK)
    }

    fun monthPeriod(): Period {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        setDayStart(cal)
        val first = cal.time
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        setDayEnd(cal)
        val last = cal.time
        return Period(first, last, Period.TYPE_MONTH)
    }

    fun yearPeriod(): Period {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        setDayStart(cal)
        val first = cal.time
        cal.set(Calendar.MONTH, Calendar.DECEMBER)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        setDayEnd(cal)
        val last = cal.time
        return Period(first, last, Period.TYPE_YEAR)
    }

    fun allTimePeriod(): Period {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, 2000)
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        setDayStart(cal)
        val first = cal.time
        cal.set(Calendar.YEAR, 3000)
        cal.set(Calendar.MONTH, Calendar.DECEMBER)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        setDayEnd(cal)
        val last = cal.time
        return Period(first, last, Period.TYPE_ALL_TIME)
    }

    private fun setDayStart(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }

    private fun setDayEnd(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
    }
}
