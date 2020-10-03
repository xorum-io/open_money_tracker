package com.blogspot.e_kanivets.moneytracker.util.validator;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class for Transfer validation.
 * Created on 13.12.2016.
 *
 * @author Evgenii Kanivets
 */

@SuppressWarnings("WeakerAccess")
public class ExchangeRatePairValidator implements IValidator<ExchangeRatePair> {

    @NonNull
    private final Context context;

    @BindView(R.id.spinner_from_currency)
    AppCompatSpinner spinnerFromCurrency;
    @BindView(R.id.spinner_to_currency)
    AppCompatSpinner spinnerToCurrency;
    @BindView(R.id.til_buy)
    TextInputLayout tilBuy;
    @BindView(R.id.et_buy)
    EditText etBuy;
    @BindView(R.id.til_sell)
    TextInputLayout tilSell;
    @BindView(R.id.et_sell)
    EditText etSell;

    public ExchangeRatePairValidator(@NonNull Context context, @NonNull View view) {
        this.context = context;
        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Override
    public boolean validate() {
        boolean valid = true;

        String fromCurrency = null;
        if (spinnerFromCurrency.isEnabled()) {
            fromCurrency = (String) spinnerFromCurrency.getSelectedItem();
        } else {
            valid = false;
        }

        String toCurrency = null;
        if (spinnerToCurrency.isEnabled()) {
            toCurrency = (String) spinnerToCurrency.getSelectedItem();
        } else {
            valid = false;
        }

        if (fromCurrency != null && toCurrency != null && fromCurrency.equals(toCurrency)) {
            Toast.makeText(context, R.string.same_currencies, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        double amountBuy = Double.MAX_VALUE;
        try {
            amountBuy = Double.parseDouble(etBuy.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (amountBuy == Double.MAX_VALUE) {
            tilBuy.setError(context.getString(R.string.field_cant_be_empty));
            amountBuy = 0;
            valid = false;
        }

        if (amountBuy > MAX_ABS_VALUE) {
            tilBuy.setError(context.getString(R.string.too_much_for_exchange));
            valid = false;
        }

        double amountSell = Double.MAX_VALUE;
        try {
            amountSell = Double.parseDouble(etSell.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (amountSell == Double.MAX_VALUE) {
            tilSell.setError(context.getString(R.string.field_cant_be_empty));
            amountSell = 0;
            valid = false;
        }

        if (amountSell > MAX_ABS_VALUE) {
            tilSell.setError(context.getString(R.string.too_much_for_exchange));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        etBuy.addTextChangedListener(new ClearErrorTextWatcher(tilBuy));
        etSell.addTextChangedListener(new ClearErrorTextWatcher(tilSell));
    }
}
