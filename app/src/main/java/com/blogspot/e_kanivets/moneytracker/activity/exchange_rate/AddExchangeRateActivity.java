package com.blogspot.e_kanivets.moneytracker.activity.exchange_rate;

import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddExchangeRateBinding;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;
import com.blogspot.e_kanivets.moneytracker.util.validator.ExchangeRatePairValidator;
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AddExchangeRateActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddExchangeRateActivity";

    public static final String KEY_EXCHANGE_RATE = "key_exchange_rate";

    @Inject
    ExchangeRateController exchangeRateController;
    @Inject
    CurrencyController currencyController;
    @Inject
    FormatController formatController;

    private IValidator<ExchangeRatePair> exchangeRatePairValidator;

    // This field passed from Intent and may be used for presetting from/to spinner values
    @Nullable
    private ExchangeRatePair exchangeRatePair;

    private ActivityAddExchangeRateBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddExchangeRateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initToolbar();
        initViews();
    }

    private boolean initData() {
        getAppComponent().inject(AddExchangeRateActivity.this);
        return true;
    }

    private void initViews() {
        exchangeRatePairValidator = new ExchangeRatePairValidator(AddExchangeRateActivity.this, binding);
        List<String> currencyList = currencyController.readAll();

        if (currencyList.size() == 0) {
            currencyList.add(getString(R.string.none));
            binding.spinnerFromCurrency.setEnabled(false);
            binding.spinnerToCurrency.setEnabled(false);
        }

        binding.spinnerFromCurrency.setAdapter(new ArrayAdapter<>(AddExchangeRateActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyList)));

        binding.spinnerToCurrency.setAdapter(new ArrayAdapter<>(AddExchangeRateActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyList)));

        // Set selections from passed ExchangeRate
        if (exchangeRatePair != null) {
            for (int i = 0; i < currencyList.size(); i++) {
                if (currencyList.get(i).equals(exchangeRatePair.getFromCurrency())) {
                    binding.spinnerFromCurrency.setSelection(i);
                }
                if (currencyList.get(i).equals(exchangeRatePair.getToCurrency())) {
                    binding.spinnerToCurrency.setSelection(i);
                }
            }

            binding.etBuy.setText(formatController.formatPrecisionNone(exchangeRatePair.getAmountBuy()));
            binding.etSell.setText(formatController.formatPrecisionNone(exchangeRatePair.getAmountSell()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_exchange_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            tryAddExchangeRate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tryAddExchangeRate() {
        CrashlyticsProxy.get().logButton("Done Exchange Rate");
        if (addExchangeRate()) {
            CrashlyticsProxy.get().logEvent("Done Exchange Rate");
            setResult(RESULT_OK);
            finish();
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean addExchangeRate() {
        if (exchangeRatePairValidator.validate()) {
            String fromCurrency = (String) binding.spinnerFromCurrency.getSelectedItem();
            String toCurrency = (String) binding.spinnerToCurrency.getSelectedItem();
            double amountBuy = Double.parseDouble(binding.etBuy.getText().toString().trim());
            double amountSell = Double.parseDouble(binding.etSell.getText().toString().trim());

            return exchangeRateController.createExchangeRatePair(
                    new ExchangeRatePair(fromCurrency, toCurrency, amountBuy, amountSell)) != null;
        } else {
            return false;
        }
    }
}
