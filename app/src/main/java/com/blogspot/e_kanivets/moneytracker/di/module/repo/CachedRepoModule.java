package com.blogspot.e_kanivets.moneytracker.di.module.repo;

import android.content.Context;
import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.entity.data.Account;
import com.blogspot.e_kanivets.moneytracker.entity.data.Category;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;
import com.blogspot.e_kanivets.moneytracker.entity.data.Record;
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer;
import com.blogspot.e_kanivets.moneytracker.repo.data.AccountRepo;
import com.blogspot.e_kanivets.moneytracker.repo.data.CategoryRepo;
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper;
import com.blogspot.e_kanivets.moneytracker.repo.data.ExchangeRateRepo;
import com.blogspot.e_kanivets.moneytracker.repo.data.RecordRepo;
import com.blogspot.e_kanivets.moneytracker.repo.data.TransferRepo;
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo;
import com.blogspot.e_kanivets.moneytracker.repo.cache.BaseCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module to provide {@link IRepo} dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module
public class CachedRepoModule {
    private Context context;

    public CachedRepoModule(Context context) {
        this.context = context;
    }

    @Provides
    @NonNull
    @Singleton
    public DbHelper providesDbHelper() {
        return new DbHelper(context);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Account> providesAccountRepo(DbHelper dbHelper) {
        return new BaseCache<>(new AccountRepo(dbHelper));
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Category> providesCategoryRepo(DbHelper dbHelper) {
        return new BaseCache<>(new CategoryRepo(dbHelper));
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<ExchangeRate> providesExchangeRateRepo(DbHelper dbHelper) {
        return new BaseCache<>(new ExchangeRateRepo(dbHelper));
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Record> providesRecordRepo(DbHelper dbHelper) {
        return new BaseCache<>(new RecordRepo(dbHelper));
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Transfer> providesTransferRepo(DbHelper dbHelper) {
        return new BaseCache<>(new TransferRepo(dbHelper));
    }
}
