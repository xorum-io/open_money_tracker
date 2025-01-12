package com.blogspot.e_kanivets.moneytracker.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.adapter.RecordReportAdapter
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityReportBinding
import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.RecordReportItem
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter
import java.util.*
import javax.inject.Inject

class ReportActivity : BaseBackActivity() {

    @Inject
    lateinit var recordController: RecordController
    @Inject
    lateinit var rateController: ExchangeRateController
    @Inject
    lateinit var currencyController: CurrencyController

    private var recordList: List<Record> = listOf()
    private var period: Period? = null
    private lateinit var adapter: RecordReportAdapter
    private lateinit var recordReportConverter: RecordReportConverter

    private lateinit var shortSummaryPresenter: ShortSummaryPresenter

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initToolbar()
        initViews()
    }

    private fun initData(): Boolean {
        appComponent.inject(this)

        period = intent.getParcelableExtra(KEY_PERIOD)
        if (period == null) return false

        recordList = recordController.getRecordsForPeriod(period)
        shortSummaryPresenter = ShortSummaryPresenter(this)
        adapter = RecordReportAdapter(mutableListOf(), hashMapOf(), this)
        recordReportConverter = RecordReportConverter()

        return true
    }

    private fun initViews() {
        initSpinnerCurrency()

        adapter.setSummaryView(shortSummaryPresenter.create(false, null))
        binding.recyclerView.adapter = adapter
    }

    private fun update(currency: String) {
        val reportMaker = ReportMaker(rateController)
        val report = reportMaker.getRecordReport(currency, period, recordList)

        adapter.setData(recordReportConverter.getItemsFromReport(report), recordReportConverter.getDataFromReport(report))
        shortSummaryPresenter.update(report, currency, reportMaker.currencyNeeded(currency, recordList))
    }

    private fun initSpinnerCurrency() {
        val currencyList = currencyController.readAll()

        binding.spinnerCurrency.adapter = ArrayAdapter(this, R.layout.view_spinner_item, currencyList)
        binding.spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) =
                    update(binding.spinnerCurrency.selectedItem.toString())

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val currency = currencyController.readDefaultCurrency()

        binding.spinnerCurrency.setSelection(currencyList.indexOf(currency))
    }

    class RecordReportConverter {

        @Inject
        lateinit var formatController: FormatController

        init {
            MtApp.get().appComponent.inject(this)
        }

        fun getItemsFromReport(report: IRecordReport?): MutableList<RecordReportItem> {
            val items: MutableList<RecordReportItem> = mutableListOf()

            if (report == null) return items

            for (categoryRecord in report.summary) {
                val parentRow = RecordReportItem.ParentRow(categoryRecord.title, formatController.formatSignedAmount(categoryRecord.amount), false)
                items.add(parentRow)
            }
            return items
        }

        fun getDataFromReport(report: IRecordReport?): HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>> {
            val data: HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>> = hashMapOf()

            if (report == null) return data

            for (categoryRecord in report.summary) {
                val parentRow = RecordReportItem.ParentRow(categoryRecord.title, formatController.formatSignedAmount(categoryRecord.amount), false)
                val childRows: MutableList<RecordReportItem.ChildRow> = mutableListOf()

                for (summaryRecord in categoryRecord.summaryRecordList) {
                    val childRow = RecordReportItem.ChildRow(summaryRecord.title, formatController.formatSignedAmount(summaryRecord.amount))
                    childRows.add(childRow)
                }

                data[parentRow] = childRows
            }
            return data
        }

    }

    companion object {

        const val KEY_PERIOD = "key_period"
    }
}
