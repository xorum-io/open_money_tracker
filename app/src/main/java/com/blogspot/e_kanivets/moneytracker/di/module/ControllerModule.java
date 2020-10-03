package com.blogspot.e_kanivets.moneytracker.di.module;

import android.content.Context;
import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.controller.backup.BackupController;
import com.blogspot.e_kanivets.moneytracker.controller.external.ExportController;
import com.blogspot.e_kanivets.moneytracker.controller.FormatController;
import com.blogspot.e_kanivets.moneytracker.controller.PeriodController;
import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController;
import com.blogspot.e_kanivets.moneytracker.controller.data.CategoryController;
import com.blogspot.e_kanivets.moneytracker.controller.CurrencyController;
import com.blogspot.e_kanivets.moneytracker.controller.data.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController;
import com.blogspot.e_kanivets.moneytracker.controller.data.RecordController;
import com.blogspot.e_kanivets.moneytracker.controller.data.TransferController;
import com.blogspot.e_kanivets.moneytracker.controller.external.ImportController;
import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module to provide Controllers dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module public class ControllerModule {
    private Context context;

    public ControllerModule(Context context) {
        this.context = context;
    }

    @Provides @NonNull @Singleton public AccountController providesAccountController(IRepo<Account> accountRepo,
            PreferenceController preferenceController) {
        return new AccountController(accountRepo, preferenceController);
    }

    @Provides @NonNull @Singleton public CategoryController providesCategoryController(IRepo<Category> categoryRepo,
            PreferenceController preferenceController) {
        return new CategoryController(categoryRepo, preferenceController);
    }

    @Provides @NonNull @Singleton
    public ExchangeRateController providesExchangeRateController(IRepo<ExchangeRate> exchangeRateRepo) {
        return new ExchangeRateController(exchangeRateRepo);
    }

    @Provides @NonNull @Singleton
    public RecordController providesRecordController(IRepo<Record> recordRepo, CategoryController categoryController,
            AccountController accountController, PreferenceController preferenceController) {
        return new RecordController(recordRepo, categoryController, accountController, preferenceController);
    }

    @Provides @NonNull @Singleton public TransferController providesTransferController(IRepo<Transfer> transferRepo,
            AccountController accountController) {
        return new TransferController(transferRepo, accountController);
    }

    @Provides @NonNull @Singleton
    public CurrencyController providesCurrencyController(AccountController accountController,
            PreferenceController preferenceController) {
        return new CurrencyController(accountController, preferenceController);
    }

    @Provides @NonNull @Singleton public PreferenceController providesPreferenceController() {
        return new PreferenceController(context);
    }

    @Provides @NonNull @Singleton
    public PeriodController providesPeriodController(PreferenceController preferenceController) {
        return new PeriodController(preferenceController);
    }

    @Provides @NonNull @Singleton
    public FormatController providesFormatController(PreferenceController preferenceController) {
        return new FormatController(preferenceController);
    }

    @Provides @NonNull @Singleton public ExportController providesExportController(RecordController recordController,
            CategoryController categoryController) {
        return new ExportController(recordController, categoryController);
    }

    @Provides @NonNull @Singleton public ImportController providesImportController(RecordController recordController) {
        return new ImportController(recordController);
    }

    @Provides @NonNull @Singleton public BackupController providesBackupController(FormatController formatController) {
        return new BackupController(formatController, context.getApplicationInfo().dataDir);
    }
}
