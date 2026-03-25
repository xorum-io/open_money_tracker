package com.blogspot.e_kanivets.moneytracker.ui.presenter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.databinding.ViewSummaryRecordsBinding
import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.report.record.IRecordReport
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter
import java.text.SimpleDateFormat
import javax.inject.Inject

class ShortSummaryPresenter(context: Context) : BaseSummaryPresenter() {

    @Inject lateinit var formatController: FormatController

    private var red: Int
    private var green: Int
    private lateinit var binding: ViewSummaryRecordsBinding

    init {
        this.context = context
        MtApp.instance.appComponent.inject(this)
        layoutInflater = LayoutInflater.from(context)
        red = context.resources.getColor(R.color.red)
        green = context.resources.getColor(R.color.green)
    }

    fun interface ItemClickListener {
        fun invoke()
    }

    fun create(shortSummary: Boolean, itemClickListener: ItemClickListener?): View {
        binding = ViewSummaryRecordsBinding.inflate(layoutInflater)
        binding.ivMore.visibility = if (shortSummary) View.VISIBLE else View.INVISIBLE
        binding.lvSummary.isClickable = false
        binding.cvSummary.isClickable = true
        val view = binding.root
        view.isEnabled = false
        view.tag = ViewHolder(binding, itemClickListener)
        return view
    }

    fun update(report: IRecordReport?, currency: String, ratesNeeded: List<String>) {
        val viewHolder = binding.root.tag as ViewHolder
        if (report == null) {
            viewHolder.tvTotalIncome.text = ""
            viewHolder.tvTotalExpense.text = ""
            viewHolder.tvTotal.setTextColor(red)
            viewHolder.tvTotal.text = createRatesNeededList(currency, ratesNeeded)
        } else {
            viewHolder.tvPeriod.text = formatPeriod(report.period)
            viewHolder.tvTotalIncome.setTextColor(if (report.totalIncome >= 0) green else red)
            viewHolder.tvTotalIncome.text = formatController.formatIncome(report.totalIncome, report.currency)
            viewHolder.tvTotalExpense.setTextColor(if (report.totalExpense > 0) green else red)
            viewHolder.tvTotalExpense.text = formatController.formatExpense(report.totalExpense, report.currency)
            viewHolder.tvTotal.setTextColor(if (report.total >= 0) green else red)
            viewHolder.tvTotal.text = formatController.formatIncome(report.total, report.currency)
        }
    }

    private fun formatPeriod(period: Period): String = when (period.type) {
        Period.TYPE_DAY -> period.firstDay
        Period.TYPE_MONTH -> SimpleDateFormat("MMMM, yyyy").format(period.first)
        Period.TYPE_YEAR -> SimpleDateFormat("yyyy").format(period.first)
        Period.TYPE_ALL_TIME -> context.getString(R.string.all_time)
        else -> context.getString(R.string.period_from_to, period.firstDay, period.lastDay)
    }

    class ViewHolder(binding: ViewSummaryRecordsBinding, itemClickListener: ItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
        val tvPeriod: TextView = binding.tvPeriod
        val tvTotalIncome: TextView = binding.tvTotalIncome
        val tvTotalExpense: TextView = binding.tvTotalExpense
        val tvTotal: TextView = binding.tvTotal
        init {
            binding.root.findViewById<View>(R.id.cvSummary).setOnClickListener {
                itemClickListener?.invoke()
            }
        }
    }
}
