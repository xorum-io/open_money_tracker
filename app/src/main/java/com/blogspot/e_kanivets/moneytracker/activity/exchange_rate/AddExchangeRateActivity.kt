package com.blogspot.e_kanivets.moneytracker.activity.exchange_rate

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddExchangeRateBinding
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.blogspot.e_kanivets.moneytracker.util.validator.ExchangeRatePairValidator
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator
import java.util.ArrayList
import org.koin.android.ext.android.inject

class AddExchangeRateActivity : BaseBackActivity() {

    companion object {
        const val KEY_EXCHANGE_RATE = "key_exchange_rate"
    }

    private val exchangeRateController: ExchangeRateController by inject()
    private val currencyController: CurrencyController by inject()
    private val formatController: FormatController by inject()

    private lateinit var exchangeRatePairValidator: IValidator<ExchangeRatePair>

    // This field passed from Intent and may be used for presetting from/to spinner values
    private var exchangeRatePair: ExchangeRatePair? = null

    private lateinit var binding: ActivityAddExchangeRateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddExchangeRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initToolbar()
        initViews()
    }

    private fun initData(): Boolean {
        exchangeRatePair = intent.getParcelableExtra(KEY_EXCHANGE_RATE)
        return true
    }

    private fun initViews() {
        exchangeRatePairValidator = ExchangeRatePairValidator(this, binding)
        val currencyList = currencyController.readAll().toMutableList()

        if (currencyList.isEmpty()) {
            currencyList.add(getString(R.string.none))
            binding.spinnerFromCurrency.isEnabled = false
            binding.spinnerToCurrency.isEnabled = false
        }

        binding.spinnerFromCurrency.adapter = ArrayAdapter(
            this,
            R.layout.view_spinner_item,
            ArrayList(currencyList)
        )

        binding.spinnerToCurrency.adapter = ArrayAdapter(
            this,
            R.layout.view_spinner_item,
            ArrayList(currencyList)
        )

        // Set selections from passed ExchangeRate
        exchangeRatePair?.let { pair ->
            for (i in currencyList.indices) {
                if (currencyList[i] == pair.fromCurrency) {
                    binding.spinnerFromCurrency.setSelection(i)
                }
                if (currencyList[i] == pair.toCurrency) {
                    binding.spinnerToCurrency.setSelection(i)
                }
            }

            binding.etBuy.setText(formatController.formatPrecisionNone(pair.amountBuy))
            binding.etSell.setText(formatController.formatPrecisionNone(pair.amountSell))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_exchange_rate, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            tryAddExchangeRate()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun tryAddExchangeRate() {
        CrashlyticsProxy.instance.logButton("Try Exchange Rate")
        if (addExchangeRate()) {
            CrashlyticsProxy.instance.logEvent("Done Exchange Rate")
            setResult(RESULT_OK)
            finish()
        }
    }

    @Suppress("SimplifiableIfStatement")
    private fun addExchangeRate() = if (exchangeRatePairValidator.validate()) {
        val fromCurrency = binding.spinnerFromCurrency.selectedItem as String
        val toCurrency = binding.spinnerToCurrency.selectedItem as String
        val amountBuy = binding.etBuy.text.toString().trim().toDouble()
        val amountSell = binding.etSell.text.toString().trim().toDouble()

        exchangeRateController.createExchangeRatePair(
            ExchangeRatePair(fromCurrency, toCurrency, amountBuy, amountSell)
        ) != null
    } else {
        false
    }
}
