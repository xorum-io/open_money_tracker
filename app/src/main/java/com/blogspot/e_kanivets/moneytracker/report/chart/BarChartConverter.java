package com.blogspot.e_kanivets.moneytracker.report.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.R;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Util class to convert {@link IMonthReport} to {@link com.github.mikephil.charting.charts.BarChart}
 * input data.
 * Created on 4/27/16.
 *
 * @author Evgenii Kanivets
 */
public class BarChartConverter {
    private final IMonthReport report;

    private final int green;
    private final int red;
    private final String incomesTitle;
    private final String expensesTitle;

    @SuppressWarnings("deprecation")
    public BarChartConverter(@NonNull Context context, @NonNull IMonthReport report) {
        this.report = report;

        green = context.getResources().getColor(R.color.green_light);
        red = context.getResources().getColor(R.color.red_light);
        incomesTitle = context.getString(R.string.incomes);
        expensesTitle = context.getString(R.string.expenses);
    }

    @NonNull
    public List<String> getXAxisValueList() {
        List<String> valueList = new ArrayList<>();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM yy");
        for (long timestamp : report.getMonthList()) {
            valueList.add(sdf.format(new Date(timestamp)));
        }

        return valueList;
    }

    @NonNull
    public List<IBarDataSet> getBarDataSetList() {
        List<BarEntry> incomeList = new ArrayList<>();
        for (int i = 0; i < report.getIncomeList().size(); i++) {
            incomeList.add(new BarEntry(report.getIncomeList().get(i).floatValue(), i));
        }

        BarDataSet incomeDataSet = new BarDataSet(incomeList, incomesTitle);
        incomeDataSet.setColor(green);

        List<BarEntry> expenseList = new ArrayList<>();
        for (int i = 0; i < report.getExpenseList().size(); i++) {
            expenseList.add(new BarEntry(report.getExpenseList().get(i).floatValue(), i));
        }

        BarDataSet dataSet2 = new BarDataSet(expenseList, expensesTitle);
        dataSet2.setColor(red);

        List<IBarDataSet> list = new ArrayList<>();
        list.add(incomeDataSet);
        list.add(dataSet2);

        return list;
    }
}
