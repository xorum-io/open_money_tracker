package com.blogspot.e_kanivets.moneytracker.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.databinding.ViewSummaryAccountsBinding
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter
import javax.inject.Inject

class AccountsSummaryPresenter(context: Context) : BaseSummaryPresenter() {

    @Inject lateinit var rateController: ExchangeRateController
    @Inject lateinit var accountController: AccountController
    @Inject lateinit var currencyController: CurrencyController
    @Inject lateinit var formatController: FormatController

    private var red: Int
    private var green: Int

    private lateinit var view: View
    private val reportMaker: ReportMaker

    init {
        this.context = context
        layoutInflater = LayoutInflater.from(context)
        red = context.resources.getColor(R.color.red)
        green = context.resources.getColor(R.color.green)
        MtApp.get().appComponent.inject(this)
        reportMaker = ReportMaker(rateController)
    }

    fun create(): View {
        val binding = ViewSummaryAccountsBinding.inflate(layoutInflater)
        view = binding.root
        view.tag = binding

        val currencyList = currencyController.readAll()
        binding.spinnerCurrency.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, currencyList)

        val currency = currencyController.readDefaultCurrency()
        for ((i, item) in currencyList.withIndex()) {
            if (item == currency) {
                binding.spinnerCurrency.setSelection(i)
                break
            }
        }

        binding.spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                update()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return view
    }

    fun update() {
        val binding = view.tag as? ViewSummaryAccountsBinding ?: return
        val currency = binding.spinnerCurrency.selectedItem as String
        val report = reportMaker.getAccountsReport(currency, accountController.readAll())
        if (report == null) {
            binding.tvTotal.setTextColor(red)
            binding.tvTotal.text = createRatesNeededList(currency, reportMaker.currencyNeededAccounts(currency, accountController.readAll()))
            binding.tvCurrency.text = ""
        } else {
            binding.tvTotal.setTextColor(if (report.total >= 0) green else red)
            binding.tvTotal.text = formatController.formatSignedAmount(report.total)
            binding.tvCurrency.setTextColor(if (report.total >= 0) green else red)
            binding.tvCurrency.text = report.currency
        }
    }
}
