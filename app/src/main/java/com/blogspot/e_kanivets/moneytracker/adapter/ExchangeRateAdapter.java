package com.blogspot.e_kanivets.moneytracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.databinding.ViewExchangeRateBinding;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;

import java.util.List;

import javax.inject.Inject;

public class ExchangeRateAdapter extends BaseAdapter {
    @Inject
    FormatController formatController;

    private Context context;
    private List<ExchangeRatePair> exchangeRates;

    public ExchangeRateAdapter(Context context, List<ExchangeRatePair> exchangeRates) {
        this.context = context;
        this.exchangeRates = exchangeRates;
        MtApp.get().getAppComponent().inject(ExchangeRateAdapter.this);
    }

    @Override
    public int getCount() {
        return exchangeRates.size();
    }

    @Override
    public ExchangeRatePair getItem(int position) {
        return exchangeRates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewExchangeRateBinding binding;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            binding = ViewExchangeRateBinding.inflate(layoutInflater, parent, false);
            convertView = binding.getRoot();

            convertView.setTag(binding);
        } else {
            binding = (ViewExchangeRateBinding) convertView.getTag();
        }

        ExchangeRatePair rate = getItem(position);

        binding.tvFromCurrency.setText(rate.getFromCurrency());
        binding.tvToCurrency.setText(rate.getToCurrency());
        binding.tvAmountBuy.setText(formatController.formatPrecisionNone(rate.getAmountBuy()));
        binding.tvAmountSell.setText(formatController.formatPrecisionNone(rate.getAmountSell()));

        return convertView;
    }
}
