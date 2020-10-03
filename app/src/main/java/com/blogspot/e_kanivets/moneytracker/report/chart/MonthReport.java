package com.blogspot.e_kanivets.moneytracker.report.chart;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.report.base.IExchangeRateProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * First {@link IMonthReport} implementation.
 * Created on 4/28/16.
 *
 * @author Evgenii Kanivets
 */
public class MonthReport implements IMonthReport {
    private final String currency;

    private final List<MonthNode> monthList;

    public MonthReport(List<Record> recordList, String currency, IExchangeRateProvider rateProvider) {
        if (recordList == null || currency == null || rateProvider == null)
            throw new NullPointerException("Params can't be null");

        this.currency = currency;

        monthList = generateReport(recordList, rateProvider);
    }

    protected MonthReport(Parcel in) {
        currency = in.readString();
        monthList = in.createTypedArrayList(MonthNode.CREATOR);
    }

    @NonNull
    @Override
    public String getCurrency() {
        return currency;
    }

    @NonNull
    @Override
    public List<Long> getMonthList() {
        List<Long> resultList = new ArrayList<>();

        for (MonthNode node : monthList) {
            resultList.add(node.getTimestamp());
        }

        return resultList;
    }

    @NonNull
    @Override
    public List<Double> getIncomeList() {
        List<Double> resultList = new ArrayList<>();

        for (MonthNode node : monthList) {
            resultList.add(node.getTotalIncome());
        }

        return resultList;
    }

    @NonNull
    @Override
    public List<Double> getExpenseList() {
        List<Double> resultList = new ArrayList<>();

        for (MonthNode node : monthList) {
            resultList.add(node.getTotalExpense());
        }

        return resultList;
    }

    /**
     * @param recordList to generate report on
     * @return sorted by timestamp list of {@link MonthNode}
     */
    @NonNull
    private List<MonthNode> generateReport(List<Record> recordList, IExchangeRateProvider rateProvider) {
        SortedMap<Long, MonthNode> monthMap = new TreeMap<>();

        for (Record record : recordList) {
            long timestamp = getMonthTimestamp(record.getTime());

            if (monthMap.get(timestamp) == null) monthMap.put(timestamp, new MonthNode(timestamp));
            MonthNode node = monthMap.get(timestamp);

            double convertedPrice = record.getFullPrice();
            if (!currency.equals(record.getCurrency())) {
                ExchangeRate exchangeRate = rateProvider.getRate(record);
                if (exchangeRate == null) throw new NullPointerException("No exchange rate found");
                convertedPrice *= exchangeRate.getAmount();
            }

            switch (record.getType()) {
                case Record.TYPE_INCOME:
                    node.addIncome(convertedPrice);
                    break;

                case Record.TYPE_EXPENSE:
                    node.addExpense(convertedPrice);
                    break;

                default:
                    break;
            }
        }

        List<MonthNode> resultList = new ArrayList<>();
        for (Long timestamp : monthMap.keySet()) {
            resultList.add(monthMap.get(timestamp));
        }

        return resultList;
    }

    private long getMonthTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
        dest.writeTypedList(monthList);
    }

    private static class MonthNode implements Parcelable {
        private long timestamp;
        private double totalIncome;
        private double totalExpense;

        public MonthNode(long timestamp) {
            this.timestamp = timestamp;
            this.totalExpense = 0;
            this.totalIncome = 0;
        }

        protected MonthNode(Parcel in) {
            timestamp = in.readLong();
            totalIncome = in.readDouble();
            totalExpense = in.readDouble();
        }

        public static final Creator<MonthNode> CREATOR = new Creator<MonthNode>() {
            @Override
            public MonthNode createFromParcel(Parcel in) {
                return new MonthNode(in);
            }

            @Override
            public MonthNode[] newArray(int size) {
                return new MonthNode[size];
            }
        };

        void addIncome(double income) {
            totalIncome += income;
        }

        void addExpense(double expense) {
            totalExpense += expense;
        }

        long getTimestamp() {
            return timestamp;
        }

        double getTotalIncome() {
            return totalIncome;
        }

        double getTotalExpense() {
            return totalExpense;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(timestamp);
            dest.writeDouble(totalIncome);
            dest.writeDouble(totalExpense);
        }
    }
}
