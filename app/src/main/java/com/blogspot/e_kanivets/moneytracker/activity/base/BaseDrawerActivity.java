package com.blogspot.e_kanivets.moneytracker.activity.base;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.charts.ChartsActivity;
import com.blogspot.e_kanivets.moneytracker.activity.external.BackupActivity;
import com.blogspot.e_kanivets.moneytracker.activity.external.ImportExportActivity;
import com.blogspot.e_kanivets.moneytracker.activity.SettingsActivity;
import com.blogspot.e_kanivets.moneytracker.activity.account.AccountsActivity;
import com.blogspot.e_kanivets.moneytracker.activity.exchange_rate.ExchangeRatesActivity;
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy;

import butterknife.BindView;

/**
 * Base implementation of {@link androidx.appcompat.app.AppCompatActivity} to encapsulate Navigation
 * Drawer logic.
 * Created on 3/16/16.
 *
 * @author Evgenii Kanivets
 */
public abstract class BaseDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_ACCOUNTS = 1;
    private static final int REQUEST_RATES = 2;
    private static final int REQUEST_SETTINGS = 3;
    private static final int REQUEST_IMPORT_EXPORT = 4;
    protected static final int REQUEST_BACKUP = 5;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    protected abstract void update();

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    @Override
    protected Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        if (drawer != null) drawer.setDrawerListener(toggle);
        toggle.syncState();

        return toolbar;
    }

    @Override
    protected void initViews() {
        super.initViews();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_accounts:
                showAccounts();
                break;

            case R.id.nav_rates:
                showRates();
                break;

            case R.id.nav_charts:
                showCharts();
                break;

            case R.id.nav_backup:
                showBackup();
                break;

            case R.id.nav_import_export:
                showImportExport();
                break;

            case R.id.nav_settings:
                showSettings();
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ACCOUNTS:
                    update();
                    break;

                case REQUEST_RATES:
                    update();
                    break;

                case REQUEST_SETTINGS:
                    update();
                    break;

                case REQUEST_IMPORT_EXPORT:
                    update();
                    break;

                default:
                    break;
            }
        }
    }

    private void showAccounts() {
        CrashlyticsProxy.get().logButton("Show Accounts");
        startActivityForResult(new Intent(BaseDrawerActivity.this, AccountsActivity.class),
                REQUEST_ACCOUNTS);
    }

    private void showRates() {
        CrashlyticsProxy.get().logButton("Show Rates");
        startActivityForResult(new Intent(BaseDrawerActivity.this, ExchangeRatesActivity.class),
                REQUEST_RATES);
    }

    private void showCharts() {
        CrashlyticsProxy.get().logButton("Show Charts");
        startActivity(new Intent(BaseDrawerActivity.this, ChartsActivity.class));
    }

    private void showBackup() {
        CrashlyticsProxy.get().logButton("Show Backup");
        startActivityForResult(new Intent(BaseDrawerActivity.this, BackupActivity.class),
                REQUEST_BACKUP);
    }

    private void showImportExport() {
        CrashlyticsProxy.get().logButton("Show Import Export");
        startActivityForResult(new Intent(BaseDrawerActivity.this, ImportExportActivity.class),
                REQUEST_IMPORT_EXPORT);
    }

    private void showSettings() {
        CrashlyticsProxy.get().logButton("Show Settings");
        startActivityForResult(new Intent(BaseDrawerActivity.this, SettingsActivity.class),
                REQUEST_SETTINGS);
    }
}
