package com.blogspot.e_kanivets.moneytracker.di.module

import android.content.Context
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController
import com.blogspot.e_kanivets.moneytracker.controller.FormatController
import com.blogspot.e_kanivets.moneytracker.controller.PeriodController
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController
import com.blogspot.e_kanivets.moneytracker.controller.external.ExportController
import com.blogspot.e_kanivets.moneytracker.controller.external.ImportController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger 2 module to provide Controllers dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module
class ControllerModule(private val context: Context) {

    @Provides @Singleton
    fun providesAccountController(
        accountRepo: IRepo<Account>,
        preferenceController: PreferenceController
    ): AccountController = AccountController(accountRepo, preferenceController)

    @Provides @Singleton
    fun providesCategoryController(
        categoryRepo: IRepo<Category>,
        preferenceController: PreferenceController
    ): CategoryController = CategoryController(categoryRepo, preferenceController)

    @Provides @Singleton
    fun providesExchangeRateController(
        exchangeRateRepo: IRepo<ExchangeRate>
    ): ExchangeRateController = ExchangeRateController(exchangeRateRepo)

    @Provides @Singleton
    fun providesRecordController(
        recordRepo: IRepo<Record>,
        categoryController: CategoryController,
        accountController: AccountController,
        preferenceController: PreferenceController
    ): RecordController = RecordController(recordRepo, categoryController, accountController, preferenceController)

    @Provides @Singleton
    fun providesTransferController(
        transferRepo: IRepo<Transfer>,
        accountController: AccountController
    ): TransferController = TransferController(transferRepo, accountController)

    @Provides @Singleton
    fun providesCurrencyController(
        accountController: AccountController,
        preferenceController: PreferenceController
    ): CurrencyController = CurrencyController(accountController, preferenceController)

    @Provides @Singleton
    fun providesPreferenceController(): PreferenceController = PreferenceController(context)

    @Provides @Singleton
    fun providesPeriodController(
        preferenceController: PreferenceController
    ): PeriodController = PeriodController(preferenceController)

    @Provides @Singleton
    fun providesFormatController(
        preferenceController: PreferenceController
    ): FormatController = FormatController(preferenceController)

    @Provides @Singleton
    fun providesExportController(
        recordController: RecordController,
        categoryController: CategoryController
    ): ExportController = ExportController(recordController, categoryController)

    @Provides @Singleton
    fun providesImportController(
        recordController: RecordController
    ): ImportController = ImportController(recordController)

    @Provides @Singleton
    fun providesBackupController(
        formatController: FormatController
    ): BackupController = BackupController(formatController, context.applicationInfo.dataDir)
}
