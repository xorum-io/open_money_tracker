package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.databinding.ViewMonthSummaryBinding;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class MonthSummaryAdapter extends BaseAdapter {

    @Inject
    FormatController formatController;

    @NonNull
    private Context context;
    @NonNull
    private IMonthReport monthReport;
    @NonNull
    private final SimpleDateFormat dateFormat;

    @SuppressLint("SimpleDateFormat")
    public MonthSummaryAdapter(@NonNull Context context, @NonNull IMonthReport monthReport) {
        MtApp.get().getAppComponent().inject(MonthSummaryAdapter.this);

        this.context = context;
        this.monthReport = monthReport;

        if (monthReport.getMonthList().size() != monthReport.getIncomeList().size()
                || monthReport.getIncomeList().size() != monthReport.getExpenseList().size()) {
            throw new IllegalArgumentException("Broken report data");
        }

        dateFormat = new SimpleDateFormat("MMM, yyyy");
    }

    @Override
    public int getCount() {
        return monthReport.getMonthList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewMonthSummaryBinding binding;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            binding = ViewMonthSummaryBinding.inflate(layoutInflater, parent, false);
            convertView = binding.getRoot();

            convertView.setTag(binding);
        } else {
            binding = (ViewMonthSummaryBinding) convertView.getTag();
        }

        // Reverse a report
        int index = monthReport.getMonthList().size() - position - 1;

        String month = dateFormat.format(monthReport.getMonthList().get(index));
        double totalIncome = monthReport.getIncomeList().get(index);
        double totalExpense = monthReport.getExpenseList().get(index);

        binding.tvMonth.setText(month);
        binding.tvTotalIncome.setText(formatController.formatSignedAmount(totalIncome));
        binding.tvTotalExpense.setText(formatController.formatSignedAmount(-totalExpense));

        return convertView;
    }
}
