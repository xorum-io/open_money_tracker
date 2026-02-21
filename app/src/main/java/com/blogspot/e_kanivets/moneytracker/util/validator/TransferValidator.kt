package com.blogspot.e_kanivets.moneytracker.util.validator

import android.content.Context
import android.widget.Toast
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityTransferBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer

class TransferValidator(
    private val context: Context,
    private val binding: ActivityTransferBinding
) : IValidator<Transfer> {

    init {
        initTextWatchers()
    }

    override fun validate(): Boolean {
        var valid = true

        if (!binding.spinnerFrom.isEnabled) {
            valid = false
        }

        if (!binding.spinnerTo.isEnabled) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show()
            valid = false
        }

        var fromAmount = Double.MAX_VALUE
        try {
            fromAmount = binding.etFromAmount.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        if (fromAmount == Double.MAX_VALUE) {
            binding.tilFromAmount.error = context.getString(R.string.field_cant_be_empty)
            fromAmount = 0.0
            valid = false
        }

        if (fromAmount > IValidator.MAX_ABS_VALUE) {
            binding.tilFromAmount.error = context.getString(R.string.too_much_for_transfer)
            valid = false
        }

        var toAmount = Double.MAX_VALUE
        try {
            toAmount = binding.etToAmount.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        if (toAmount == Double.MAX_VALUE) {
            binding.tilToAmount.error = context.getString(R.string.field_cant_be_empty)
            toAmount = 0.0
            valid = false
        }

        if (toAmount > IValidator.MAX_ABS_VALUE) {
            binding.tilToAmount.error = context.getString(R.string.too_much_for_transfer)
            valid = false
        }

        return valid
    }

    private fun initTextWatchers() {
        binding.etFromAmount.addTextChangedListener(ClearErrorTextWatcher(binding.tilFromAmount))
        binding.etToAmount.addTextChangedListener(ClearErrorTextWatcher(binding.tilToAmount))
    }
}
