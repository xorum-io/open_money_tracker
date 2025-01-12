package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddExchangeRateBinding;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;

@SuppressWarnings("WeakerAccess")
public class ExchangeRatePairValidator implements IValidator<ExchangeRatePair> {

    @NonNull
    private final Context context;

    private ActivityAddExchangeRateBinding binding;

    public ExchangeRatePairValidator(
            @NonNull Context context,
            @NonNull ActivityAddExchangeRateBinding binding
    ) {
        this.context = context;
        this.binding = binding;
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        String fromCurrency = null;
        if (binding.spinnerFromCurrency.isEnabled()) {
            fromCurrency = (String) binding.spinnerFromCurrency.getSelectedItem();
        } else {
            valid = false;
        }

        String toCurrency = null;
        if (binding.spinnerToCurrency.isEnabled()) {
            toCurrency = (String) binding.spinnerToCurrency.getSelectedItem();
        } else {
            valid = false;
        }

        if (fromCurrency != null && toCurrency != null && fromCurrency.equals(toCurrency)) {
            Toast.makeText(context, R.string.same_currencies, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        double amountBuy = Double.MAX_VALUE;
        try {
            amountBuy = Double.parseDouble(binding.etBuy.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (amountBuy == Double.MAX_VALUE) {
            binding.tilBuy.setError(context.getString(R.string.field_cant_be_empty));
            amountBuy = 0;
            valid = false;
        }

        if (amountBuy > MAX_ABS_VALUE) {
            binding.tilBuy.setError(context.getString(R.string.too_much_for_exchange));
            valid = false;
        }

        double amountSell = Double.MAX_VALUE;
        try {
            amountSell = Double.parseDouble(binding.etSell.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (amountSell == Double.MAX_VALUE) {
            binding.tilSell.setError(context.getString(R.string.field_cant_be_empty));
            amountSell = 0;
            valid = false;
        }

        if (amountSell > MAX_ABS_VALUE) {
            binding.tilSell.setError(context.getString(R.string.too_much_for_exchange));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        binding.etBuy.addTextChangedListener(new ClearErrorTextWatcher(binding.tilBuy));
        binding.etSell.addTextChangedListener(new ClearErrorTextWatcher(binding.tilSell));
    }
}
