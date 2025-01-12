package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.databinding.ViewReportItemBinding
import com.blogspot.e_kanivets.moneytracker.databinding.ViewReportItemExpBinding
import com.blogspot.e_kanivets.moneytracker.entity.RecordReportItem

class RecordReportAdapter(
    private var items: MutableList<RecordReportItem>,
    private var data: HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>>,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var red: Int = ContextCompat.getColor(context, R.color.red)
    private var green: Int = ContextCompat.getColor(context, R.color.green)

    private lateinit var summaryViewHolder: RecyclerView.ViewHolder

    override fun getItemCount() = items.size + 1

    override fun getItemViewType(position: Int): Int = when {
        position == 0 -> TYPE_SUMMARY
        items[getPositionWithoutSummary(position)] is RecordReportItem.ChildRow -> TYPE_CHILD
        else -> TYPE_PARENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_PARENT -> ParentViewHolder(
            ViewReportItemExpBinding.inflate(LayoutInflater.from(context), parent, false),
            ::changeItems,
            context
        )

        TYPE_CHILD -> ChildViewHolder(
            ViewReportItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )

        else -> summaryViewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val posWithoutSummary = getPositionWithoutSummary(position)
        if (posWithoutSummary < 0) return

        when (holder) {
            is ChildViewHolder -> {
                val row = items[posWithoutSummary] as RecordReportItem.ChildRow
                holder.tvCategory.text = row.title
                holder.tvTotal.text = row.amount
                holder.tvTotal.setTextColor(if (row.amount.first() != '-') green else red)
            }

            is ParentViewHolder -> {
                val row = items[posWithoutSummary] as RecordReportItem.ParentRow
                holder.tvCategory.text = row.category
                holder.tvTotal.text = row.totalAmount
                holder.tvTotal.setTextColor(if (row.totalAmount.first() != '-') green else red)
            }
        }
    }

    fun setData(
        items: MutableList<RecordReportItem>,
        data: HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>>
    ) {
        this.items = items
        this.data = data
        notifyDataSetChanged()
    }

    fun setSummaryView(summaryView: View) {
        this.summaryViewHolder = summaryView.tag as RecyclerView.ViewHolder
    }

    private fun changeItems(position: Int) {
        val positionWithoutSummary = getPositionWithoutSummary(position)
        if (items[positionWithoutSummary] is RecordReportItem.ParentRow) {
            val parentRow = items[positionWithoutSummary] as RecordReportItem.ParentRow

            if (parentRow.isOpen) {
                closeParentRow(parentRow, positionWithoutSummary)
            } else {
                openParentRow(parentRow, positionWithoutSummary)
            }
        }
    }

    private fun closeParentRow(parentRow: RecordReportItem.ParentRow, position: Int) {
        val item = items.filterIndexed { index, _ -> index > position }
            .find { it is RecordReportItem.ParentRow }

        val lastChildInd = if (item != null) items.indexOf(item) else items.size

        items.subList(position + 1, lastChildInd).clear()
        val itemCount = lastChildInd - position - 1
        notifyItemRangeRemoved(getPositionWithSummary(position + 1), itemCount)

        parentRow.isOpen = false
    }

    private fun openParentRow(parentRow: RecordReportItem.ParentRow, position: Int) {
        data[parentRow]?.let { childRows ->
            var lastChildInd = position + 1
            for (childRow in childRows) {
                items.add(lastChildInd, childRow)
                lastChildInd++
            }
            notifyItemRangeInserted(getPositionWithSummary(position + 1), childRows.size)
        }

        parentRow.isOpen = true
    }

    private fun getPositionWithSummary(position: Int) = position + 1

    private fun getPositionWithoutSummary(position: Int) = position - 1

    class ParentViewHolder(
        binding: ViewReportItemExpBinding,
        changeItems: ((Int) -> Unit),
        context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {

        var tvCategory: TextView = binding.tvCategory
        var tvTotal: TextView = binding.tvTotal

        private var isOpen: Boolean = false

        init {
            binding.root.setOnClickListener {
                if (isOpen) {
                    binding.lowerDivider.visibility = View.GONE
                    binding.ivArrow.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_arrow_downward_outline
                        )
                    )
                } else {
                    binding.lowerDivider.visibility = View.VISIBLE
                    binding.ivArrow.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_arrow_upward_outline
                        )
                    )
                }
                isOpen = !isOpen
                changeItems(adapterPosition)
            }
        }
    }

    class ChildViewHolder(binding: ViewReportItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvCategory: TextView = binding.tvCategory
        val tvTotal: TextView = binding.tvTotal
    }

    companion object {

        private const val TYPE_SUMMARY = 0
        private const val TYPE_PARENT = 1
        private const val TYPE_CHILD = 2
    }
}
