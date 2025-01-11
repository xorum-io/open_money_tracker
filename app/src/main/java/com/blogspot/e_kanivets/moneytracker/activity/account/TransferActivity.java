package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityTransferBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator;
import com.blogspot.e_kanivets.moneytracker.util.validator.TransferValidator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TransferActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferActivity";

    @Inject
    TransferController transferController;
    @Inject
    AccountController accountController;

    private IValidator<Transfer> transferValidator;

    private List<Account> accountList;

    private ActivityTransferBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTransferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initToolbar();
        initViews();
    }

    private boolean initData() {
        getAppComponent().inject(TransferActivity.this);
        accountList = accountController.readActiveAccounts();
        return true;
    }

    private void initViews() {
        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        transferValidator = new TransferValidator(TransferActivity.this, binding);

        if (accounts.size() == 0) {
            accounts.add(getString(R.string.none));
            binding.spinnerFrom.setEnabled(false);
            binding.spinnerTo.setEnabled(false);
        }

        binding.spinnerFrom.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));

        binding.spinnerTo.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            tryTransfer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tryTransfer() {
        CrashlyticsProxy.get().logButton("Done Transfer");
        if (doTransfer()) {
            CrashlyticsProxy.get().logEvent("Done Transfer");
            setResult(RESULT_OK);
            finish();
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean doTransfer() {
        if (transferValidator.validate()) {
            Account fromAccount = accountList.get(binding.spinnerFrom.getSelectedItemPosition());
            Account toAccount = accountList.get(binding.spinnerTo.getSelectedItemPosition());
            double fromAmount = Double.parseDouble(binding.etFromAmount.getText().toString());
            double toAmount = Double.parseDouble(binding.etToAmount.getText().toString());

            return transferController.create(new Transfer(System.currentTimeMillis(),
                    fromAccount.getId(), toAccount.getId(), fromAmount, toAmount)) != null;
        } else {
            return false;
        }
    }
}
