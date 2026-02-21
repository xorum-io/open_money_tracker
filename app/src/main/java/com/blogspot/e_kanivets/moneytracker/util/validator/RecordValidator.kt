package com.blogspot.e_kanivets.moneytracker.util.validator

import android.content.Context
import android.widget.Toast
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddRecordBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Record

class RecordValidator(
    private val context: Context,
    private val binding: ActivityAddRecordBinding
) : IValidator<Record> {

    init {
        initTextWatchers()
    }

    override fun validate(): Boolean {
        var valid = true

        val category = binding.etCategory.text.toString().trim()

        if (category.isEmpty()) {
            binding.tilCategory.error = context.getString(R.string.field_cant_be_empty)
            valid = false
        }

        var price = Double.MAX_VALUE
        try {
            price = binding.etPrice.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        if (price == Double.MAX_VALUE) {
            binding.tilPrice.error = context.getString(R.string.field_cant_be_empty)
            price = 0.0
            valid = false
        }

        if (price > IValidator.MAX_ABS_VALUE) {
            binding.tilPrice.error = context.getString(R.string.too_rich)
            valid = false
        }

        if (!binding.spinnerAccount.isEnabled) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }

    private fun initTextWatchers() {
        binding.etPrice.addTextChangedListener(ClearErrorTextWatcher(binding.tilPrice))
        binding.etTitle.addTextChangedListener(ClearErrorTextWatcher(binding.tilTitle))
        binding.etCategory.addTextChangedListener(ClearErrorTextWatcher(binding.tilCategory))
    }
}
