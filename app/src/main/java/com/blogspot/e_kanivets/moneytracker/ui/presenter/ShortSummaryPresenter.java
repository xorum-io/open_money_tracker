package com.blogspot.e_kanivets.moneytracker.ui.presenter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.databinding.ViewSummaryRecordsBinding;
import com.blogspot.e_kanivets.moneytracker.entity.Period;
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

public class ShortSummaryPresenter extends BaseSummaryPresenter {

    @Inject
    FormatController formatController;

    private int red;
    private int green;
    private ViewSummaryRecordsBinding binding;

    public ShortSummaryPresenter(Context context) {
        this.context = context;
        MtApp.get().getAppComponent().inject(ShortSummaryPresenter.this);

        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    public interface ItemClickListener {
        void invoke();
    }

    public View create(boolean shortSummary, ItemClickListener itemClickListener) {
        binding = ViewSummaryRecordsBinding.inflate(layoutInflater);

        binding.ivMore.setVisibility(shortSummary ? View.VISIBLE : View.INVISIBLE);
        binding.lvSummary.setClickable(false);
        binding.cvSummary.setClickable(true);

        View view = binding.getRoot();
        view.setEnabled(false);
        view.setTag(new ViewHolder(binding, itemClickListener));

        return view;
    }

    public void update(IRecordReport report, String currency, List<String> ratesNeeded) {
        ViewHolder viewHolder = (ViewHolder) binding.getRoot().getTag();
        if (report == null) {
            viewHolder.tvTotalIncome.setText("");
            viewHolder.tvTotalExpense.setText("");

            viewHolder.tvTotal.setTextColor(red);
            viewHolder.tvTotal.setText(createRatesNeededList(currency, ratesNeeded));
        } else {
            viewHolder.tvPeriod.setText(formatPeriod(report.getPeriod()));

            viewHolder.tvTotalIncome.setTextColor(report.getTotalIncome() >= 0 ? green : red);
            viewHolder.tvTotalIncome.setText(formatController.formatIncome(report.getTotalIncome(),
                    report.getCurrency()));

            viewHolder.tvTotalExpense.setTextColor(report.getTotalExpense() > 0 ? green : red);
            viewHolder.tvTotalExpense.setText(formatController.formatExpense(report.getTotalExpense(),
                    report.getCurrency()));

            viewHolder.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.tvTotal.setText(formatController.formatIncome(report.getTotal(),
                    report.getCurrency()));
        }
    }

    private String formatPeriod(Period period) {
        switch (period.getType()) {
            case Period.TYPE_DAY:
                return period.getFirstDay();

            case Period.TYPE_MONTH:
                return new SimpleDateFormat("MMMM, yyyy").format(period.getFirst());

            case Period.TYPE_YEAR:
                return new SimpleDateFormat("yyyy").format(period.getFirst());

            case Period.TYPE_ALL_TIME:
                return context.getString(R.string.all_time);

            default:
                return context.getString(R.string.period_from_to, period.getFirstDay(),
                        period.getLastDay());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPeriod;
        TextView tvTotalIncome;
        TextView tvTotalExpense;
        TextView tvTotal;

        public ViewHolder(ViewSummaryRecordsBinding binding, final ItemClickListener itemClickListener) {
            super(binding.getRoot());

            this.tvPeriod = binding.tvPeriod;
            this.tvTotalIncome = binding.tvTotalIncome;
            this.tvTotalExpense = binding.tvTotalExpense;
            this.tvTotal = binding.tvTotal;

            binding.getRoot().findViewById(R.id.cvSummary).setOnClickListener(v -> {
                if (itemClickListener != null)
                    itemClickListener.invoke();
            });
        }
    }

}
