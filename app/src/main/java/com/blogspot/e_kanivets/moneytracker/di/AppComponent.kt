package com.blogspot.e_kanivets.moneytracker.di

import com.blogspot.e_kanivets.moneytracker.activity.ReportActivity
import com.blogspot.e_kanivets.moneytracker.activity.SettingsActivity
import com.blogspot.e_kanivets.moneytracker.activity.account.AccountsActivity
import com.blogspot.e_kanivets.moneytracker.activity.account.AddAccountActivity
import com.blogspot.e_kanivets.moneytracker.activity.account.TransferActivity
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.EditAccountActivity
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment.AccountOperationsFragment
import com.blogspot.e_kanivets.moneytracker.activity.account.edit.fragment.EditAccountFragment
import com.blogspot.e_kanivets.moneytracker.activity.charts.ChartsActivity
import com.blogspot.e_kanivets.moneytracker.activity.exchange_rate.AddExchangeRateActivity
import com.blogspot.e_kanivets.moneytracker.activity.exchange_rate.ExchangeRatesActivity
import com.blogspot.e_kanivets.moneytracker.activity.external.BackupActivity
import com.blogspot.e_kanivets.moneytracker.activity.external.ImportExportActivity
import com.blogspot.e_kanivets.moneytracker.activity.record.AddRecordActivity
import com.blogspot.e_kanivets.moneytracker.activity.record.MainActivity
import com.blogspot.e_kanivets.moneytracker.adapter.AccountAdapter
import com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter
import com.blogspot.e_kanivets.moneytracker.adapter.MonthSummaryAdapter
import com.blogspot.e_kanivets.moneytracker.adapter.RecordAdapter
import com.blogspot.e_kanivets.moneytracker.di.module.ControllerModule
import com.blogspot.e_kanivets.moneytracker.di.module.repo.CachedRepoModule
import com.blogspot.e_kanivets.moneytracker.ui.AppRateDialog
import com.blogspot.e_kanivets.moneytracker.ui.PeriodSpinner
import com.blogspot.e_kanivets.moneytracker.ui.presenter.AccountsSummaryPresenter
import com.blogspot.e_kanivets.moneytracker.ui.presenter.ShortSummaryPresenter
import com.blogspot.e_kanivets.moneytracker.util.RecordItemsBuilder
import dagger.Component
import javax.inject.Singleton

/**
 * Dagger 2 component.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Component(modules = [CachedRepoModule::class, ControllerModule::class])
@Singleton
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(addRecordActivity: AddRecordActivity)
    fun inject(exchangeRatesActivity: ExchangeRatesActivity)
    fun inject(exchangeRateActivity: AddExchangeRateActivity)
    fun inject(accountsActivity: AccountsActivity)
    fun inject(addAccountActivity: AddAccountActivity)
    fun inject(transferActivity: TransferActivity)
    fun inject(importExportActivity: ImportExportActivity)
    fun inject(reportActivity: ReportActivity)
    fun inject(chartsActivity: ChartsActivity)
    fun inject(backupActivity: BackupActivity)
    fun inject(settingsFragment: SettingsActivity.SettingsFragment)
    fun inject(accountsSummaryPresenter: AccountsSummaryPresenter)
    fun inject(appRateDialog: AppRateDialog)
    fun inject(periodSpinner: PeriodSpinner)
    fun inject(recordAdapter: RecordAdapter)
    fun inject(accountAdapter: AccountAdapter)
    fun inject(shortSummaryPresenter: ShortSummaryPresenter)
    fun inject(exchangeRateAdapter: ExchangeRateAdapter)
    fun inject(monthSummaryAdapter: MonthSummaryAdapter)
    fun inject(editAccountActivity: EditAccountActivity)
    fun inject(editAccountFragment: EditAccountFragment)
    fun inject(accountRecordsFragment: AccountOperationsFragment)
    fun inject(recordItemsBuilder: RecordItemsBuilder)
    fun inject(recordReportConverter: ReportActivity.RecordReportConverter)
}
