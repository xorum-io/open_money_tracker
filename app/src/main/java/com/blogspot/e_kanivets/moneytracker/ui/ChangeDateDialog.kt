package com.blogspot.e_kanivets.moneytracker.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.blogspot.e_kanivets.moneytracker.databinding.DialogChangeDateBinding
import java.util.Calendar
import java.util.Date

class ChangeDateDialog(
    context: Context,
    private var date: Date,
    private var listener: OnDateChangedListener
) : AlertDialog(context) {

    private lateinit var binding: DialogChangeDateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogChangeDateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cal = Calendar.getInstance()
        cal.time = date
        binding.datePicker.init(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH),
            null
        )
        binding.bOk.setOnClickListener { ok() }
        binding.bCancel.setOnClickListener { dismiss() }
    }

    fun ok() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, binding.datePicker.year)
        cal.set(Calendar.MONTH, binding.datePicker.month)
        cal.set(Calendar.DAY_OF_MONTH, binding.datePicker.dayOfMonth)
        listener.onDateChanged(cal.time)
        dismiss()
    }

    interface OnDateChangedListener {
        fun onDateChanged(date: Date)
    }
}
