package com.blogspot.e_kanivets.moneytracker.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.databinding.ViewSummaryAccountsBinding;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.account.IAccountsReport;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter;

import java.util.List;

import javax.inject.Inject;

public class AccountsSummaryPresenter extends BaseSummaryPresenter {

    @Inject
    ExchangeRateController rateController;
    @Inject
    AccountController accountController;
    @Inject
    CurrencyController currencyController;
    @Inject
    FormatController formatController;

    private int red;
    private int green;
    private View view;
    private final ReportMaker reportMaker;

    @SuppressWarnings("deprecation")
    public AccountsSummaryPresenter(Context context) {
        this.context = context;

        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);

        MtApp.get().getAppComponent().inject(AccountsSummaryPresenter.this);
        reportMaker = new ReportMaker(rateController);
    }

    public View create() {
        ViewSummaryAccountsBinding binding = ViewSummaryAccountsBinding.inflate(layoutInflater);
        view = binding.getRoot();
        view.setTag(binding);

        List<String> currencyList = currencyController.readAll();

        binding.spinnerCurrency.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, currencyList));

        String currency = currencyController.readDefaultCurrency();

        for (int i = 0; i < currencyList.size(); i++) {
            String item = currencyList.get(i);

            if (item.equals(currency)) {
                binding.spinnerCurrency.setSelection(i);
                break;
            }
        }

        binding.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void update() {
        ViewSummaryAccountsBinding binding = (ViewSummaryAccountsBinding) view.getTag();

        String currency = (String) binding.spinnerCurrency.getSelectedItem();
        IAccountsReport report = reportMaker.getAccountsReport(currency, accountController.readAll());

        if (report == null) {
            binding.tvTotal.setTextColor(red);
            binding.tvTotal.setText(createRatesNeededList(currency,
                    reportMaker.currencyNeededAccounts(currency, accountController.readAll())));
            binding.tvCurrency.setText("");
        } else {
            binding.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
            binding.tvTotal.setText(formatController.formatSignedAmount(report.getTotal()));
            binding.tvCurrency.setTextColor(report.getTotal() >= 0 ? green : red);
            binding.tvCurrency.setText(report.getCurrency());
        }
    }
}
