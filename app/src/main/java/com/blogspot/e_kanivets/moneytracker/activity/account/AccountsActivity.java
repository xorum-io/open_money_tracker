package com.blogspot.e_kanivets.moneytracker.activity.account;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.EditAccountActivity;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.AccountAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.databinding.ActivityAccountsBinding;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.AccountsSummaryPresenter;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;

import javax.inject.Inject;

public class AccountsActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountsActivity";

    private static final int REQUEST_ADD_ACCOUNT = 1;
    private static final int REQUEST_TRANSFER = 2;
    private static final int REQUEST_EDIT_ACCOUNT = 3;

    @Inject
    AccountController accountController;

    private AccountsSummaryPresenter summaryPresenter;

    private ActivityAccountsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccountsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initData();
        initToolbar();
        initViews();
    }

    private boolean initData() {
        getAppComponent().inject(AccountsActivity.this);
        summaryPresenter = new AccountsSummaryPresenter(AccountsActivity.this);
        return true;
    }

    private void initViews() {
        binding.listView.addHeaderView(summaryPresenter.create());
        binding.listView.setOnItemClickListener((adapterView, view, i, l) -> onAccountClick(i));
        binding.btnAddAccount.setOnClickListener(view -> addAccount());

        registerForContextMenu(binding.listView);
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_transfer) {
            makeTransfer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAccountClick(int position) {
        Account account = accountController.readAll().get(position - 1);
        startActivityForResult(EditAccountActivity.Companion.newIntent(this, account), REQUEST_EDIT_ACCOUNT);
    }

    public void makeTransfer() {
        CrashlyticsProxy.get().logButton("Add Transfer");
        startActivityForResult(new Intent(AccountsActivity.this, TransferActivity.class), REQUEST_TRANSFER);
    }

    public void addAccount() {
        CrashlyticsProxy.get().logButton("Add Account");
        Intent intent = new Intent(AccountsActivity.this, AddAccountActivity.class);
        startActivityForResult(intent, REQUEST_ADD_ACCOUNT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_ACCOUNT:
                    update();
                    break;

                case REQUEST_TRANSFER:

                case REQUEST_EDIT_ACCOUNT:
                    update();
                    setResult(RESULT_OK);
                    break;

                default:
                    break;
            }
        }
    }

    private void update() {
        binding.listView.setAdapter(new AccountAdapter(AccountsActivity.this, accountController.readAll()));
        summaryPresenter.update();
    }
}
