package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.entity.RecordItem
import kotlinx.android.synthetic.main.view_header_date.view.*
import kotlinx.android.synthetic.main.view_record.view.*
import javax.inject.Inject

class RecordAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Inject
    lateinit var formatController: FormatController

    var itemClickListener: ((Int) -> Unit)? = null

    private var whiteRed: Int
    private var whiteGreen: Int
    private var red: Int
    private var green: Int

    private var items: List<RecordItem>
    private var context: Context

    private var isSummaryViewNeeded: Boolean = false
    lateinit var summaryViewHolder: RecyclerView.ViewHolder

    constructor(context: Context, items: List<RecordItem>, isSummaryViewNeeded: Boolean) {
        this.context = context
        this.items = items

        MtApp.get().appComponent.inject(this)

        whiteRed = ContextCompat.getColor(context, R.color.white_red)
        whiteGreen = ContextCompat.getColor(context, R.color.white_green)
        red = ContextCompat.getColor(context, R.color.red)
        green = ContextCompat.getColor(context, R.color.green)

        this.isSummaryViewNeeded = isSummaryViewNeeded
    }

    override fun getItemCount() = items.size + if (isSummaryViewNeeded) 1 else 0

    override fun getItemViewType(position: Int): Int = if (position == 0 && isSummaryViewNeeded) {
        TYPE_SUMMARY
    } else if (items[position - if (isSummaryViewNeeded) 1 else 0] is RecordItem.Header) {
        TYPE_HEADER
    } else {
        TYPE_RECORD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TYPE_RECORD -> RecordViewHolder(LayoutInflater.from(context).inflate(R.layout.view_record, parent, false), itemClickListener)
                TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.view_header_date, parent, false))
                else -> summaryViewHolder
            }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && isSummaryViewNeeded) {
            //view holder already bound to view
            return
        }

        if (viewHolder is RecordViewHolder) {
            val record = items[position - if (isSummaryViewNeeded) 1 else 0] as RecordItem.Record
            viewHolder.tvPrice.setTextColor(if (record.isIncome) green else red)

            val price = (if (record.isIncome) record.fullPrice else getNegative(record.fullPrice))
            viewHolder.tvPrice.text = formatController.formatSignedAmount(price)
            viewHolder.tvTitle.text = record.title
            viewHolder.tvCategory.text = record.categoryName
            viewHolder.tvCurrency.text = record.currency
        } else {
            val headerViewHolder = viewHolder as HeaderViewHolder
            val header = items[position - if (isSummaryViewNeeded) 1 else 0] as RecordItem.Header
            headerViewHolder.tvDate.text = header.date
        }
    }

    private fun getNegative(number: Double): Double {
        return -1 * number
    }

    fun setRecords(itemsList: List<RecordItem>) {
        items = itemsList
        notifyDataSetChanged()
    }

    class RecordViewHolder : RecyclerView.ViewHolder {

        var tvPrice: TextView
        var tvTitle: TextView
        var tvCategory: TextView
        var tvCurrency: TextView

        constructor(view: View, itemClickListener: ((Int) -> Unit)?) : super(view) {
            tvPrice = view.tvPrice
            tvTitle = view.tvTitle
            tvCategory = view.tvCategory
            tvCurrency = view.tvCurrency

            view.setOnClickListener {
                itemClickListener?.invoke(adapterPosition)
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.tvDate
    }

    companion object {
        private const val TYPE_SUMMARY = 0
        private const val TYPE_HEADER = 1
        private const val TYPE_RECORD = 2
    }
}
