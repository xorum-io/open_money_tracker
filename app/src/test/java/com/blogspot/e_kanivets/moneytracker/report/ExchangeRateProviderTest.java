package com.blogspot.e_kanivets.moneytracker.report;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRateProviderTest {
    private ExchangeRateController rateController;

    @Before
    public void setUp() throws Exception {
        rateController = new ExchangeRateController(new TestRepo());
    }

    @After
    public void tearDown() throws Exception {
        rateController = null;
    }

    @Test
    public void testGetRate() throws Exception {
        IExchangeRateProvider provider;

        try {
            provider = new ExchangeRateProvider(null, rateController);
        } catch (NullPointerException e) {
            provider = null;
        }

        assertNull(provider);

        try {
            provider = new ExchangeRateProvider("", null);
        } catch (NullPointerException e) {
            provider = null;
        }

        assertNull(provider);

        try {
            provider = new ExchangeRateProvider(null, null);
        } catch (NullPointerException e) {
            provider = null;
        }

        assertNull(provider);

        provider = new ExchangeRateProvider("USD", rateController);

        assertEquals(new ExchangeRate(1, "UAH", "USD", 4),
                provider.getRate(new Record(1, 0, 0, "", 1, 0, 0, "UAH", 0)));

        assertEquals(new ExchangeRate(0, "AFN", "USD", 3),
                provider.getRate(new Record(1, 0, 0, "", 1, 0, 0, "AFN", 0)));

        assertNull(provider.getRate(new Record(1, 0, 0, "", 1, 0, 0, "SMTH", 0)));
    }

    private static class TestRepo implements IRepo<ExchangeRate> {
        @Nullable
        @Override
        public ExchangeRate create(ExchangeRate instance) {
            return null;
        }

        @Nullable
        @Override
        public ExchangeRate read(long id) {
            return null;
        }

        @Nullable
        @Override
        public ExchangeRate update(ExchangeRate instance) {
            return null;
        }

        @Override
        public boolean delete(ExchangeRate instance) {
            return false;
        }

        @NonNull
        @Override
        public List<ExchangeRate> readAll() {
            List<ExchangeRate> rateList = new ArrayList<>();
            rateList.add(new ExchangeRate(1, "UAH", "USD", 4));
            rateList.add(new ExchangeRate(0, "UAH", "USD", 2));
            rateList.add(new ExchangeRate(0, "AFN", "USD", 3));
            rateList.add(new ExchangeRate(0, "USD", "EUR", 20));
            return rateList;
        }

        @NonNull
        @Override
        public List<ExchangeRate> readWithCondition(String condition, String[] args) {
            return new ArrayList<>();
        }
    }
}
