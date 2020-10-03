package com.blogspot.e_kanivets.moneytracker.activity.charts.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.adapter.MonthSummaryAdapter;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {
    private static final String ARG_MONTH_REPORT = "arg_month_report";

    @Nullable
    private IMonthReport monthReport;

    @BindView(R.id.listView)
    ListView listView;

    public SummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param monthReport report for some period grouped by months.
     * @return A new instance of fragment SummaryFragment.
     */
    public static SummaryFragment newInstance(@Nullable IMonthReport monthReport) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MONTH_REPORT, monthReport);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            monthReport = getArguments().getParcelable(ARG_MONTH_REPORT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(@Nullable View rootView) {
        if (rootView == null) return;
        ButterKnife.bind(this, rootView);

        if (monthReport != null) {
            listView.setAdapter(new MonthSummaryAdapter(getActivity(), monthReport));
        }
    }
}
