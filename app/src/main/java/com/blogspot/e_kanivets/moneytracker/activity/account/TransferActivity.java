package com.blogspot.e_kanivets.moneytracker.activity.account;

import androidx.appcompat.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;
import com.blogspot.e_kanivets.moneytracker.util.validator.IValidator;
import com.blogspot.e_kanivets.moneytracker.util.validator.TransferValidator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TransferActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferActivity";

    @Inject
    TransferController transferController;
    @Inject
    AccountController accountController;

    private IValidator<Transfer> transferValidator;

    private List<Account> accountList;

    @BindView(R.id.contentView)
    View contentView;
    @BindView(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @BindView(R.id.spinner_to)
    AppCompatSpinner spinnerTo;
    @BindView(R.id.et_from_amount)
    EditText etFromAmount;
    @BindView(R.id.et_to_amount)
    EditText etToAmount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(TransferActivity.this);
        accountList = accountController.readActiveAccounts();
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        transferValidator = new TransferValidator(TransferActivity.this, contentView);

        if (accounts.size() == 0) {
            accounts.add(getString(R.string.none));
            spinnerFrom.setEnabled(false);
            spinnerTo.setEnabled(false);
        }

        spinnerFrom.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));

        spinnerTo.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                tryTransfer();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
            Account fromAccount = accountList.get(spinnerFrom.getSelectedItemPosition());
            Account toAccount = accountList.get(spinnerTo.getSelectedItemPosition());
            double fromAmount = Double.parseDouble(etFromAmount.getText().toString());
            double toAmount = Double.parseDouble(etToAmount.getText().toString());

            return transferController.create(new Transfer(System.currentTimeMillis(),
                    fromAccount.getId(), toAccount.getId(), fromAmount, toAmount)) != null;
        } else {
            return false;
        }
    }
}
