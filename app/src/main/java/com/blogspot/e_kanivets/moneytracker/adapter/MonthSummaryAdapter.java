package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter to display a report grouped by months.
 * Created on 1/13/17.
 *
 * @author Evgenii Kanivets
 */

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
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(R.layout.view_month_summary, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        // Reverse a report
        int index = monthReport.getMonthList().size() - position - 1;

        String month = dateFormat.format(monthReport.getMonthList().get(index));
        double totalIncome = monthReport.getIncomeList().get(index);
        double totalExpense = monthReport.getExpenseList().get(index);

        viewHolder.tvMonth.setText(month);
        viewHolder.tvTotalIncome.setText(formatController.formatSignedAmount(totalIncome));
        viewHolder.tvTotalExpense.setText(formatController.formatSignedAmount(-totalExpense));

        return convertView;
    }

    public static class ViewHolder {
        @BindView(R.id.tvMonth)
        TextView tvMonth;
        @BindView(R.id.tvTotalIncome)
        TextView tvTotalIncome;
        @BindView(R.id.tvTotalExpense)
        TextView tvTotalExpense;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
