package com.blogspot.e_kanivets.moneytracker.report.record.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public class CategoryRecord {
    private String title;
    private String currency;
    private double amount;
    private List<SummaryRecord> summaryRecordList;

    public CategoryRecord(String title, String currency, double amount) {
        this.title = title;
        this.currency = currency;
        this.amount = amount;
        this.summaryRecordList = new ArrayList<>();
    }

    public void add(@NonNull SummaryRecord summaryRecord) {
        summaryRecordList.add(summaryRecord);
    }

    public String getTitle() {
        return title;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public List<SummaryRecord> getSummaryRecordList() {
        return summaryRecordList;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CategoryRecord) {
            CategoryRecord categoryRecord = (CategoryRecord) o;
            return this.title.equals(categoryRecord.getTitle())
                    && this.currency.equals(categoryRecord.getCurrency())
                    && this.amount == categoryRecord.getAmount()
                    && this.summaryRecordList.equals(categoryRecord.getSummaryRecordList());
        } else return false;
    }
}
