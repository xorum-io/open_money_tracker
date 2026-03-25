package com.blogspot.e_kanivets.moneytracker.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.controller.PeriodController
import com.blogspot.e_kanivets.moneytracker.entity.Period
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class PeriodSpinner(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatSpinner(context, attrs, defStyleAttr) {

    @Inject
    lateinit var periodController: PeriodController

    var periodSelectedListener: OnPeriodSelectedListener? = null
    private val listener: OnItemSelectedListener
    private var lastPeriod: Period? = null

    constructor(context: Context) : this(context, null, android.R.attr.spinnerStyle)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.spinnerStyle)

    init {
        MtApp.instance.appComponent.inject(this)

        adapter = ArrayAdapter(
            context, android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.array_periods)
        )
        listener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> updatePeriod(periodController.dayPeriod())
                    1 -> updatePeriod(periodController.weekPeriod())
                    2 -> updatePeriod(periodController.monthPeriod())
                    3 -> updatePeriod(periodController.yearPeriod())
                    4 -> updatePeriod(periodController.allTimePeriod())
                    5 -> showFromDateDialog()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun updatePeriod(period: Period) {
        if (lastPeriod != null && lastPeriod == period) return
        periodSelectedListener?.onPeriodSelected(period)
        lastPeriod = period
    }

    fun setPeriod(period: Period) {
        when (period.type) {
            Period.TYPE_DAY -> setSelection(0)
            Period.TYPE_WEEK -> setSelection(1)
            Period.TYPE_MONTH -> setSelection(2)
            Period.TYPE_YEAR -> setSelection(3)
            Period.TYPE_ALL_TIME -> setSelection(4)
            Period.TYPE_CUSTOM -> {
                super.setSelection(5)
                updatePeriod(period)
            }
        }
    }

    override fun setSelection(position: Int) {
        super.setSelection(position)
        listener.onItemSelected(null, null, position, 0)
    }

    private fun showFromDateDialog() {
        if (lastPeriod == null) return
        val dialog = ChangeDateDialog(
            context, lastPeriod!!.first,
            object : ChangeDateDialog.OnDateChangedListener {
                override fun onDateChanged(fromDate: Date) {
                    val cal = Calendar.getInstance()
                    cal.time = fromDate
                    cal.set(Calendar.HOUR_OF_DAY, 0)
                    cal.set(Calendar.MINUTE, 0)
                    cal.set(Calendar.SECOND, 0)
                    cal.set(Calendar.MILLISECOND, 0)
                    showToDateDialog(cal.time)
                }
            })
        dialog.show()
    }

    private fun showToDateDialog(fromDate: Date) {
        if (lastPeriod == null) return
        val dialog = ChangeDateDialog(
            context, lastPeriod!!.last,
            object : ChangeDateDialog.OnDateChangedListener {
                override fun onDateChanged(toDate: Date) {
                    val cal = Calendar.getInstance()
                    cal.time = toDate
                    cal.set(Calendar.HOUR_OF_DAY, 23)
                    cal.set(Calendar.MINUTE, 59)
                    cal.set(Calendar.SECOND, 59)
                    cal.set(Calendar.MILLISECOND, 999)
                    if (cal.time.time < fromDate.time) {
                        Toast.makeText(context, R.string.start_earlier_end, Toast.LENGTH_SHORT).show()
                    } else {
                        updatePeriod(Period(fromDate, cal.time, Period.TYPE_CUSTOM))
                    }
                }
            })
        dialog.show()
    }

    fun interface OnPeriodSelectedListener {
        fun onPeriodSelected(period: Period)
    }
}
