package com.blogspot.e_kanivets.moneytracker.ui.presenter;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.MtApp;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.report.ReportMaker;
import com.blogspot.e_kanivets.moneytracker.report.account.IAccountsReport;
import com.blogspot.e_kanivets.moneytracker.ui.presenter.base.BaseSummaryPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class to create and manage summary header view for .
 * Created on 2/26/16.
 *
 * @author Evgenii Kanivets
 */
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
        view = layoutInflater.inflate(R.layout.view_summary_accounts, null);

        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        List<String> currencyList = currencyController.readAll();

        viewHolder.spinnerCurrency.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, currencyList));

        String currency = currencyController.readDefaultCurrency();

        for (int i = 0; i < currencyList.size(); i++) {
            String item = currencyList.get(i);

            if (item.equals(currency)) {
                viewHolder.spinnerCurrency.setSelection(i);
                break;
            }
        }

        viewHolder.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String currency = (String) viewHolder.spinnerCurrency.getSelectedItem();
        IAccountsReport report = reportMaker.getAccountsReport(currency, accountController.readAll());

        if (report == null) {
            viewHolder.tvTotal.setTextColor(red);
            viewHolder.tvTotal.setText(createRatesNeededList(currency,
                    reportMaker.currencyNeededAccounts(currency, accountController.readAll())));
            viewHolder.tvCurrency.setText("");
        } else {
            viewHolder.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.tvTotal.setText(formatController.formatSignedAmount(report.getTotal()));
            viewHolder.tvCurrency.setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.tvCurrency.setText(report.getCurrency());
        }
    }

    public static class ViewHolder {
        @BindView(R.id.spinnerCurrency)
        AppCompatSpinner spinnerCurrency;
        @BindView(R.id.tvTotal)
        TextView tvTotal;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
