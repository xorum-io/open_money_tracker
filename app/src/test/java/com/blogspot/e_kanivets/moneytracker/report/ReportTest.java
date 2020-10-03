package com.blogspot.e_kanivets.moneytracker.report;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport;
import com.blogspot.e_kanivets.moneytracker.report.record.model.CategoryRecord;
import com.blogspot.e_kanivets.moneytracker.report.record.model.SummaryRecord;
import com.blogspot.e_kanivets.moneytracker.report.record.RecordReport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit4 test case.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public class ReportTest {
    private String currency;
    private IExchangeRateProvider rateProvider;

    @Before
    public void setUp() throws Exception {
        currency = "UAH";
        rateProvider = new TestProvider();
    }

    @After
    public void tearDown() throws Exception {
        currency = null;
        rateProvider = null;
    }

    @Test
    public void testForNulls() throws Exception {
        IRecordReport report;

        Period period = new Period(new Date(1), new Date(), Period.TYPE_CUSTOM);
        List<Record> recordList = new ArrayList<>();

        try {
            report = new RecordReport(null, period, recordList, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new RecordReport(currency, null, recordList, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new RecordReport(currency, period, null, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new RecordReport(currency, period, recordList, null);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new RecordReport(null, null, null, null);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNull(report);

        try {
            report = new RecordReport(currency, period, recordList, rateProvider);
        } catch (NullPointerException e) {
            report = null;
        }

        assertNotNull(report);
    }

    @Test
    public void testGetCurrency() throws Exception {
        Period period = new Period(new Date(1), new Date(), Period.TYPE_CUSTOM);
        List<Record> recordList = new ArrayList<>();

        IRecordReport report = new RecordReport(currency, period, recordList, rateProvider);

        assertEquals(currency, report.getCurrency());

        currency = "KHI";
        report = new RecordReport(currency, period, recordList, rateProvider);

        assertEquals(currency, report.getCurrency());
    }

    @Test
    public void testGetPeriod() throws Exception {
        Period period = new Period(new Date(1), new Date(), Period.TYPE_CUSTOM);
        List<Record> recordList = new ArrayList<>();

        IRecordReport report = new RecordReport(currency, period, recordList, rateProvider);

        assertEquals(period, report.getPeriod());

        period = new Period(new Date(3), new Date(100), Period.TYPE_CUSTOM);
        report = new RecordReport(currency, period, recordList, rateProvider);

        assertEquals(period, report.getPeriod());
    }

    @Test
    public void testGetTotal() throws Exception {
        Period period = new Period(new Date(1), new Date(), Period.TYPE_CUSTOM);

        List<Record> recordList = getRecordList();

        IRecordReport report = new RecordReport(currency, period, recordList, rateProvider);

        double expectedTotal = 10 * 4 - 2 + 5 - 10 * 4;
        assertEquals(expectedTotal, report.getTotal(), 0.0000000001);
    }

    @Test
    public void testGetTotalIncome() throws Exception {
        Period period = new Period(new Date(1), new Date(), Period.TYPE_CUSTOM);

        List<Record> recordList = getRecordList();

        IRecordReport report = new RecordReport(currency, period, recordList, rateProvider);

        double expectedTotal = 10 * 4 + 5;
        assertEquals(expectedTotal, report.getTotalIncome(), 0.0000000001);
    }

    @Test
    public void testGetTotalExpense() throws Exception {
        Period period = new Period(new Date(1), new Date(), Period.TYPE_CUSTOM);

        List<Record> recordList = getRecordList();

        IRecordReport report = new RecordReport(currency, period, recordList, rateProvider);

        double expectedTotal = -2 - 10 * 4;
        assertEquals(expectedTotal, report.getTotalExpense(), 0.0000000001);
    }

    @Test
    public void testGetSummary() throws Exception {
        Period period = new Period(new Date(1), new Date(), Period.TYPE_CUSTOM);

        List<Record> recordList = new ArrayList<>();

        Category category = new Category(1, "category");
        Account account1 = new Account(1, "account1", 100, "UAH", 0, 0, false, 0);
        Account account2 = new Account(2, "account2", 100, "USD", 0, 0, false, 0);

        Record record1 = new Record(1, 0, Record.TYPE_INCOME, "1", category, 10, account2, "USD");
        recordList.add(record1);
        Record record2 = new Record(2, 1, Record.TYPE_EXPENSE, "1", category, 2, account1, "UAH");
        recordList.add(record2);
        Record record3 = new Record(3, 2, Record.TYPE_INCOME, "3", category, 5, account1, "UAH");
        recordList.add(record3);
        Record record4 = new Record(4, 3, Record.TYPE_EXPENSE, "4", category, 10, account2, "USD");
        recordList.add(record4);

        IRecordReport report = new RecordReport(currency, period, recordList, rateProvider);

        List<CategoryRecord> categoryRecordList = new ArrayList<>();

        CategoryRecord categoryRecord = new CategoryRecord("category", currency, 10 * 4 - 2 + 5 - 10 * 4);

        SummaryRecord summaryRecord1 = new SummaryRecord("1", currency, 38);
        Record convertedRecord1 = new Record(record1.getId(), record1.getTime(), record1.getType(),
                record1.getTitle(), record1.getCategory(), 40, record1.getAccount(), currency);
        summaryRecord1.add(convertedRecord1);
        summaryRecord1.add(record2);
        categoryRecord.add(summaryRecord1);

        SummaryRecord summaryRecord2 = new SummaryRecord("3", currency, 5);
        summaryRecord2.add(record3);
        categoryRecord.add(summaryRecord2);

        SummaryRecord summaryRecord3 = new SummaryRecord("4", currency, -40);
        Record convertedRecord4 = new Record(record4.getId(), record4.getTime(), record4.getType(),
                record4.getTitle(), record4.getCategory(), 40, record4.getAccount(), currency);
        summaryRecord3.add(convertedRecord4);
        categoryRecord.add(summaryRecord3);

        categoryRecordList.add(categoryRecord);

        assertEquals(categoryRecordList, report.getSummary());
    }

    @NonNull
    private List<Record> getRecordList() {
        List<Record> recordList = new ArrayList<>();

        Category category = new Category(1, "category");
        Account account1 = new Account(1, "account1", 100, "UAH", 0, 0, false, 0);
        Account account2 = new Account(2, "account2", 100, "USD", 0, 0, false, 0);

        Record record1 = new Record(1, 0, Record.TYPE_INCOME, "1", category, 10, account2, "USD");
        recordList.add(record1);
        Record record2 = new Record(2, 1, Record.TYPE_EXPENSE, "1", category, 2, account1, "UAH");
        recordList.add(record2);
        Record record3 = new Record(3, 2, Record.TYPE_INCOME, "3", category, 5, account1, "UAH");
        recordList.add(record3);
        Record record4 = new Record(4, 3, Record.TYPE_EXPENSE, "4", category, 10, account2, "USD");
        recordList.add(record4);

        return recordList;
    }

    private static class TestProvider implements IExchangeRateProvider {

        @Nullable
        @Override
        public ExchangeRate getRate(@Nullable Record record) {
            if (record == null) return null;

            String fromCurrency = record.getCurrency();
            switch (fromCurrency) {
                case "USD":
                    return new ExchangeRate(1, "USD", "UAH", 4);

                case "AFN":
                    return new ExchangeRate(0, "AFN", "UAH", 3);

                default:
                    return null;

            }
        }

        @Nullable
        @Override
        public ExchangeRate getRate(@Nullable Account account) {
            return null;
        }
    }
}
