package com.blogspot.e_kanivets.moneytracker.activity.charts

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.activity.charts.fragment.GraphFragment
import com.blogspot.e_kanivets.moneytracker.activity.charts.fragment.SummaryFragment
import com.blogspot.e_kanivets.moneytracker.adapter.GeneralViewPagerAdapter
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityChartsBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport
import org.koin.android.ext.android.inject

class ChartsActivity : BaseBackActivity() {

    private val recordController: RecordController by inject()
    private val exchangeRateController: ExchangeRateController by inject()
    private val currencyController: CurrencyController by inject()

    private lateinit var binding: ActivityChartsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initViews()
    }

    private fun initViews() {
        setupViewPager(binding.viewPager)
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    private fun createRatesNeededList(currency: String, ratesNeeded: List<String>): String {
        val sb = StringBuilder(getString(R.string.error_exchange_rates))
        for (str in ratesNeeded) {
            sb.append("\n").append(str).append(getString(R.string.arrow)).append(currency)
        }
        return sb.toString()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val reportMaker = ReportMaker(exchangeRateController)
        val currency = currencyController.readDefaultCurrency()
        val recordList = recordController.readAll()
        val currencyNeeded = reportMaker.currencyNeeded(currency, recordList)

        val monthReport = reportMaker.getMonthReport(currency, recordList).takeIf { currencyNeeded.isEmpty() }

        val graphFragment = if (monthReport == null) {
            GraphFragment.newInstance(createRatesNeededList(currency, currencyNeeded))
        } else {
            GraphFragment.newInstance(monthReport)
        }

        val adapter = GeneralViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SummaryFragment.newInstance(monthReport), getString(R.string.summary))
        adapter.addFragment(graphFragment, getString(R.string.graph))
        viewPager.adapter = adapter
    }
}
