package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAddAccountBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;
import com.blogspot.e_kanivets.moneytracker.util.validator.AccountValidator;
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator;

import java.util.ArrayList;

import javax.inject.Inject;

public class AddAccountActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddAccountActivity";

    @Inject
    AccountController accountController;
    @Inject
    CurrencyController currencyController;

    private IValidator<Account> accountValidator;

    private ActivityAddAccountBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initToolbar();
        initViews();
    }

    private boolean initData() {
        getAppComponent().inject(AddAccountActivity.this);
        return true;
    }

    private void initViews() {
        accountValidator = new AccountValidator(AddAccountActivity.this, binding);
        binding.spinner.setAdapter(new ArrayAdapter<>(AddAccountActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyController.readAll())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            tryAddAccount();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tryAddAccount() {
        CrashlyticsProxy.get().logButton("Done Account");
        if (addAccount()) {
            CrashlyticsProxy.get().logEvent("Done Account");
            setResult(RESULT_OK);
            finish();
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean addAccount() {
        if (accountValidator.validate()) {
            String title = binding.etTitle.getText().toString().trim();
            double initSum = Double.parseDouble(binding.etInitSum.getText().toString().trim());
            String currency = (String) binding.spinner.getSelectedItem();
            double goal = 0;
            int color = 0;

            Account account = new Account(-1, title, initSum, currency, goal, false, color);
            return accountController.create(account) != null;
        } else {
            return false;
        }
    }
}
