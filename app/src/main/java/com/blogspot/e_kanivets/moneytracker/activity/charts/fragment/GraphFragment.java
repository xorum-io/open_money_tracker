package com.blogspot.e_kanivets.moneytracker.activity.charts.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.report.chart.BarChartConverter;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    private static final String ARG_MONTH_REPORT = "arg_month_report";
    private static final String ARG_NO_DATA_TEXT = "arg_no_data_text";

    @Nullable
    private IMonthReport monthReport;
    @Nullable
    private String noDataText;

    @BindView(R.id.bar_chart)
    BarChart barChart;

    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param monthReport report for some period grouped by months.
     * @return A new instance of fragment GraphFragment.
     */
    public static GraphFragment newInstance(@NonNull IMonthReport monthReport) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MONTH_REPORT, monthReport);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param noDataText text that will be displayed in case of error.
     * @return A new instance of fragment GraphFragment.
     */
    public static GraphFragment newInstance(@NonNull String noDataText) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NO_DATA_TEXT, noDataText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            monthReport = getArguments().getParcelable(ARG_MONTH_REPORT);
            noDataText = getArguments().getString(ARG_NO_DATA_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(@Nullable View rootView) {
        if (rootView == null) return;
        ButterKnife.bind(this, rootView);

        if (monthReport == null) {
            barChart.setNoDataText(noDataText);
        } else {
            BarChartConverter barChartConverter = new BarChartConverter(getActivity(), monthReport);

            BarData barData = new BarData(barChartConverter.getXAxisValueList(),
                    barChartConverter.getBarDataSetList());
            barData.setDrawValues(false);

            barChart.setData(barData);
            barChart.setDescription(null);
            barChart.setVisibleXRangeMinimum(8);
            barChart.setScaleYEnabled(false);
            barChart.setVisibleXRangeMaximum(34);
            barChart.setHighlightPerDragEnabled(false);
            barChart.setHighlightPerTapEnabled(false);
        }
    }
}
