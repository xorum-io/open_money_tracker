package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.databinding.ViewMonthSummaryBinding
import com.blogspot.e_kanivets.moneytracker.report.chart.IMonthReport
import java.text.SimpleDateFormat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MonthSummaryAdapter(
    private val context: Context,
    private val monthReport: IMonthReport
) : BaseAdapter(), KoinComponent {

    private val formatController: FormatController by inject()
    private val dateFormat = SimpleDateFormat("MMM, yyyy")

    init {
        if (monthReport.monthList.size != monthReport.incomeList.size ||
            monthReport.incomeList.size != monthReport.expenseList.size) {
            throw IllegalArgumentException("Broken report data")
        }
    }

    override fun getCount(): Int = monthReport.monthList.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ViewMonthSummaryBinding
        var view = convertView
        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)
            binding = ViewMonthSummaryBinding.inflate(layoutInflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = view.tag as ViewMonthSummaryBinding
        }
        // Reverse a report
        val index = monthReport.monthList.size - position - 1
        val month = dateFormat.format(monthReport.monthList[index])
        val totalIncome = monthReport.incomeList[index]
        val totalExpense = monthReport.expenseList[index]
        binding.tvMonth.text = month
        binding.tvTotalIncome.text = formatController.formatSignedAmount(totalIncome)
        binding.tvTotalExpense.text = formatController.formatSignedAmount(-totalExpense)
        return view
    }
}
