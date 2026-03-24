package com.blogspot.e_kanivets.moneytracker.activity.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.blogspot.e_kanivets.moneytracker.R
import com.blogspot.e_kanivets.moneytracker.activity.SettingsActivity
import com.blogspot.e_kanivets.moneytracker.activity.account.AccountsActivity
import com.blogspot.e_kanivets.moneytracker.activity.charts.ChartsActivity
import com.blogspot.e_kanivets.moneytracker.activity.exchange_rate.ExchangeRatesActivity
import com.blogspot.e_kanivets.moneytracker.activity.external.BackupActivity
import com.blogspot.e_kanivets.moneytracker.activity.external.ImportExportActivity
import com.blogspot.e_kanivets.moneytracker.util.CrashlyticsProxy
import com.google.android.material.navigation.NavigationView

abstract class BaseDrawerActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected lateinit var drawer: DrawerLayout
    protected lateinit var navigationView: NavigationView

    protected abstract fun update()

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun initToolbar(): Toolbar? {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer?.setDrawerListener(toggle)
        toggle.syncState()

        return toolbar
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_accounts -> showAccounts()
            R.id.nav_rates -> showRates()
            R.id.nav_charts -> showCharts()
            R.id.nav_backup -> showBackup()
            R.id.nav_import_export -> showImportExport()
            R.id.nav_settings -> showSettings()
        }

        drawer.closeDrawer(GravityCompat.START)
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_ACCOUNTS,
                REQUEST_RATES,
                REQUEST_SETTINGS,
                REQUEST_IMPORT_EXPORT -> update()
            }
        }
    }

    private fun showAccounts() {
        CrashlyticsProxy.instance.logButton("Show Accounts")
        startActivityForResult(Intent(this, AccountsActivity::class.java), REQUEST_ACCOUNTS)
    }

    private fun showRates() {
        CrashlyticsProxy.instance.logButton("Show Rates")
        startActivityForResult(Intent(this, ExchangeRatesActivity::class.java), REQUEST_RATES)
    }

    private fun showCharts() {
        CrashlyticsProxy.instance.logButton("Show Charts")
        startActivity(Intent(this, ChartsActivity::class.java))
    }

    private fun showBackup() {
        CrashlyticsProxy.instance.logButton("Show Backup")
        startActivityForResult(Intent(this, BackupActivity::class.java), REQUEST_BACKUP)
    }

    private fun showImportExport() {
        CrashlyticsProxy.instance.logButton("Show Import Export")
        startActivityForResult(Intent(this, ImportExportActivity::class.java), REQUEST_IMPORT_EXPORT)
    }

    private fun showSettings() {
        CrashlyticsProxy.instance.logButton("Show Settings")
        startActivityForResult(Intent(this, SettingsActivity::class.java), REQUEST_SETTINGS)
    }

    companion object {
        private const val REQUEST_ACCOUNTS = 1
        private const val REQUEST_RATES = 2
        private const val REQUEST_SETTINGS = 3
        private const val REQUEST_IMPORT_EXPORT = 4
        const val REQUEST_BACKUP = 5
    }
}
