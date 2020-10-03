package com.blogspot.e_kanivets.moneytracker.activity.exchange_rate;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.base.BaseBackActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;
import com.blogspot.e_kanivets.moneytracker.util.ExchangeRatesSummarizer;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class ExchangeRatesActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "ExchangeRatesActivity";

    private static final int REQUEST_ADD_EXCHANGE_RATE = 1;

    @Inject
    ExchangeRateController rateController;

    private List<ExchangeRatePair> exchangeRateList;

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_exchange_rates;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(ExchangeRatesActivity.this);
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        registerForContextMenu(listView);
        update();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_exchange_rate, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                deleteExchangeRate(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteExchangeRate(int position) {
        CrashlyticsProxy.get().logButton("Delete Exchange Rate");
        rateController.deleteExchangeRatePair(exchangeRateList.get(position));
        update();
        setResult(RESULT_OK);
    }

    @OnClick(R.id.btn_add_exchange_rate)
    public void addExchangeRate() {
        CrashlyticsProxy.get().logButton("Add Exchange Rate");
        Intent intent = new Intent(ExchangeRatesActivity.this, AddExchangeRateActivity.class);
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE);
    }

    @OnItemClick(R.id.listView)
    public void addExchangeRateOnBaseOfExisted(int position) {
        CrashlyticsProxy.get().logButton("Edit Exchange Rate");
        if (position < 0 || position >= exchangeRateList.size()) return;
        Intent intent = new Intent(ExchangeRatesActivity.this, AddExchangeRateActivity.class);
        intent.putExtra(AddExchangeRateActivity.KEY_EXCHANGE_RATE, exchangeRateList.get(position));
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_EXCHANGE_RATE:
                    update();
                    setResult(RESULT_OK);
                    break;

                default:
                    break;
            }
        }
    }

    private void update() {
        exchangeRateList = new ExchangeRatesSummarizer(rateController.readAll()).getPairedSummaryList();
        Collections.reverse(exchangeRateList);

        listView.setAdapter(new ExchangeRateAdapter(ExchangeRatesActivity.this, exchangeRateList));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
}
