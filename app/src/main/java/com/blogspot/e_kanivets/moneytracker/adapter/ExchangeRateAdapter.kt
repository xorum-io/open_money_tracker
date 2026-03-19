package com.blogspot.e_kanivets.moneytracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.blogspot.e_kanivets.moneytracker.MtApp
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.databinding.ViewExchangeRateBinding
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair
import javax.inject.Inject

class ExchangeRateAdapter(
    private val context: Context,
    private val exchangeRates: List<ExchangeRatePair>
) : BaseAdapter() {

    @Inject lateinit var formatController: FormatController

    init {
        MtApp.instance.appComponent.inject(this)
    }

    override fun getCount(): Int = exchangeRates.size

    override fun getItem(position: Int): ExchangeRatePair = exchangeRates[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ViewExchangeRateBinding
        var view = convertView
        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)
            binding = ViewExchangeRateBinding.inflate(layoutInflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = view.tag as ViewExchangeRateBinding
        }
        val rate = getItem(position)
        binding.tvFromCurrency.text = rate.fromCurrency
        binding.tvToCurrency.text = rate.toCurrency
        binding.tvAmountBuy.text = formatController.formatPrecisionNone(rate.amountBuy)
        binding.tvAmountSell.text = formatController.formatPrecisionNone(rate.amountSell)
        return view
    }
}
