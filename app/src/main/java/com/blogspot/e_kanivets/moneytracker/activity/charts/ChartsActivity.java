package com.blogspot.e_kanivets.moneytracker.activity.charts;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.activity.charts.fragment.GraphFragment;
import com.blogspot.e_kanivets.moneytracker.activity.charts.fragment.SummaryFragment;
import com.blogspot.e_kanivets.moneytracker.adapter.GeneralViewPagerAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChartsActivity extends BaseBackActivity {

    @Inject RecordController recordController;
    @Inject ExchangeRateController exchangeRateController;
    @Inject CurrencyController currencyController;

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override protected int getContentViewId() {
        return R.layout.activity_charts;
    }

    @Override protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(ChartsActivity.this);
        return result;
    }

    @Override protected void initViews() {
        super.initViews();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected String createRatesNeededList(String currency, List<String> ratesNeeded) {
        StringBuilder sb = new StringBuilder(getString(R.string.error_exchange_rates));

        for (String str : ratesNeeded) {
            sb.append("\n").append(str).append(getString(R.string.arrow)).append(currency);
        }

        return sb.toString();
    }

    private void setupViewPager(ViewPager viewPager) {
        ReportMaker reportMaker = new ReportMaker(exchangeRateController);
        String currency = currencyController.readDefaultCurrency();
        List<Record> recordList = recordController.readAll();
        List<String> currencyNeeded = reportMaker.currencyNeeded(currency, recordList);

        IMonthReport monthReport = null;
        if (currencyNeeded.isEmpty()) monthReport = reportMaker.getMonthReport(currency, recordList);

        Fragment graphFragment;
        if (monthReport == null) {
            graphFragment = GraphFragment.newInstance(createRatesNeededList(currency, currencyNeeded));
        } else {
            graphFragment = GraphFragment.newInstance(monthReport);
        }

        GeneralViewPagerAdapter adapter = new GeneralViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SummaryFragment.newInstance(monthReport), getString(R.string.summary));
        adapter.addFragment(graphFragment, getString(R.string.graph));
        viewPager.setAdapter(adapter);
    }
}
