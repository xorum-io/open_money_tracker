package com.blogspot.e_kanivets.moneytracker.util.validator

import android.content.Context
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddAccountBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Account

class AccountValidator(
    private val context: Context,
    private val binding: ActivityAddAccountBinding
) : IValidator<Account> {

    init {
        initTextWatchers()
    }

    override fun validate(): Boolean {
        val title = binding.etTitle.text.toString().trim()
        var initSum = Double.MAX_VALUE

        try {
            initSum = binding.etInitSum.text.toString().trim().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        var valid = true

        if (title.isEmpty()) {
            binding.tilTitle.error = context.getString(R.string.field_cant_be_empty)
            valid = false
        }

        if (initSum == Double.MAX_VALUE) {
            binding.tilInitSum.error = context.getString(R.string.field_cant_be_empty)
            initSum = 0.0
            valid = false
        }

        if (Math.abs(initSum) > IValidator.MAX_ABS_VALUE) {
            binding.tilInitSum.error = context.getString(R.string.too_rich_or_poor)
            valid = false
        }

        return valid
    }

    private fun initTextWatchers() {
        binding.etTitle.addTextChangedListener(ClearErrorTextWatcher(binding.tilTitle))
        binding.etInitSum.addTextChangedListener(ClearErrorTextWatcher(binding.tilInitSum))
    }
}
