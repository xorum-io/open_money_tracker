package com.blogspot.e_kanivets.moneytracker.util.validator

import android.content.Context
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.databinding.FragmentEditAccountBinding
import com.blogspot.e_kanivets.moneytracker.entity.data.Account

class EditAccountValidator(
    private val context: Context,
    private val binding: FragmentEditAccountBinding
) : IValidator<Account> {

    init {
        initTextWatchers()
    }

    override fun validate(): Boolean {
        val title = binding.etTitle.text.toString().trim()
        var goal = Double.MAX_VALUE

        try {
            goal = binding.etGoal.text.toString().trim().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        var valid = true

        if (title.isEmpty()) {
            binding.tilTitle.error = context.getString(R.string.field_cant_be_empty)
            valid = false
        }

        if (goal == Double.MAX_VALUE) {
            binding.tilGoal.error = context.getString(R.string.field_cant_be_empty)
            goal = 0.0
            valid = false
        }

        if (Math.abs(goal) > IValidator.MAX_ABS_VALUE) {
            binding.tilGoal.error = context.getString(R.string.too_rich_or_poor)
            valid = false
        }

        return valid
    }

    private fun initTextWatchers() {
        binding.etTitle.addTextChangedListener(ClearErrorTextWatcher(binding.tilTitle))
        binding.etGoal.addTextChangedListener(ClearErrorTextWatcher(binding.tilGoal))
    }
}
