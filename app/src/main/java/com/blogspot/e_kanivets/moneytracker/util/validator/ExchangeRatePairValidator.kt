package com.blogspot.e_kanivets.moneytracker.util.validator

import android.content.Context
import android.widget.Toast
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddExchangeRateBinding
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair

class ExchangeRatePairValidator(
    private val context: Context,
    private val binding: ActivityAddExchangeRateBinding
) : IValidator<ExchangeRatePair> {

    init {
        initTextWatchers()
    }

    override fun validate(): Boolean {
        var valid = true

        val fromCurrency: String?
        if (binding.spinnerFromCurrency.isEnabled) {
            fromCurrency = binding.spinnerFromCurrency.selectedItem as? String
        } else {
            fromCurrency = null
            valid = false
        }

        val toCurrency: String?
        if (binding.spinnerToCurrency.isEnabled) {
            toCurrency = binding.spinnerToCurrency.selectedItem as? String
        } else {
            toCurrency = null
            valid = false
        }

        if (fromCurrency != null && toCurrency != null && fromCurrency == toCurrency) {
            Toast.makeText(context, R.string.same_currencies, Toast.LENGTH_SHORT).show()
            valid = false
        }

        var amountBuy = Double.MAX_VALUE
        try {
            amountBuy = binding.etBuy.text.toString().trim().toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (amountBuy == Double.MAX_VALUE) {
            binding.tilBuy.error = context.getString(R.string.field_cant_be_empty)
            amountBuy = 0.0
            valid = false
        }

        if (amountBuy > IValidator.MAX_ABS_VALUE) {
            binding.tilBuy.error = context.getString(R.string.too_much_for_exchange)
            valid = false
        }

        var amountSell = Double.MAX_VALUE
        try {
            amountSell = binding.etSell.text.toString().trim().toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (amountSell == Double.MAX_VALUE) {
            binding.tilSell.error = context.getString(R.string.field_cant_be_empty)
            amountSell = 0.0
            valid = false
        }

        if (amountSell > IValidator.MAX_ABS_VALUE) {
            binding.tilSell.error = context.getString(R.string.too_much_for_exchange)
            valid = false
        }

        return valid
    }

    private fun initTextWatchers() {
        binding.etBuy.addTextChangedListener(ClearErrorTextWatcher(binding.tilBuy))
        binding.etSell.addTextChangedListener(ClearErrorTextWatcher(binding.tilSell))
    }
}
