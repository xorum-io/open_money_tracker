package com.blogspot.e_kanivets.moneytracker.controller;

import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.entity.Period;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 4/22/16.
 *
 * @author Evgenii Kanivets
 */
public class PeriodControllerTest {
    private PeriodController periodController;

    @Before
    public void setUp() throws Exception {
        PreferenceController prefsMock = Mockito.mock(PreferenceController.class);
        periodController = new PeriodController(prefsMock);
    }

    @After
    public void tearDown() throws Exception {
        periodController = null;
    }

    @Test
    public void testDayPeriod() throws Exception {
        Period period = periodController.dayPeriod();
        assertEquals(Period.TYPE_DAY, period.getType());
        validateBounds(period);
    }

    @Test
    public void testWeekPeriod() throws Exception {
        Period period = periodController.weekPeriod();
        assertEquals(Period.TYPE_WEEK, period.getType());
        validateBounds(period);
    }

    @Test
    public void testMonthPeriod() throws Exception {
        Period period = periodController.monthPeriod();
        assertEquals(Period.TYPE_MONTH, period.getType());
        validateBounds(period);
    }

    @Test
    public void testYearPeriod() throws Exception {
        Period period = periodController.yearPeriod();
        assertEquals(Period.TYPE_YEAR, period.getType());
        validateBounds(period);
    }

    private boolean validateBounds(@NonNull Period period) {
        Calendar first = Calendar.getInstance();
        first.setTime(period.getFirst());

        assertEquals(0, first.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, first.get(Calendar.MINUTE));
        assertEquals(0, first.get(Calendar.SECOND));
        assertEquals(0, first.get(Calendar.MILLISECOND));

        Calendar last = Calendar.getInstance();
        last.setTime(period.getLast());

        assertEquals(23, last.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, last.get(Calendar.MINUTE));
        assertEquals(59, last.get(Calendar.SECOND));
        assertEquals(999, last.get(Calendar.MILLISECOND));

        return true;
    }
}
