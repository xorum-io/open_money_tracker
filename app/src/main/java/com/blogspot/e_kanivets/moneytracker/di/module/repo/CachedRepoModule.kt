package com.blogspot.e_kanivets.moneytracker.di.module.repo

import android.content.Context
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import com.blogspot.e_kanivets.moneytracker.repo.cache.BaseCache
import com.blogspot.e_kanivets.moneytracker.repo.data.AccountRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.CategoryRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.ExchangeRateRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.RecordRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.TransferRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger 2 module to provide [IRepo] dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module
class CachedRepoModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesDbHelper(): DbHelper = DbHelper(context)

    @Provides
    @Singleton
    fun providesAccountRepo(dbHelper: DbHelper): IRepo<Account> = BaseCache(AccountRepo(dbHelper))

    @Provides
    @Singleton
    fun providesCategoryRepo(dbHelper: DbHelper): IRepo<Category> = BaseCache(CategoryRepo(dbHelper))

    @Provides
    @Singleton
    fun providesExchangeRateRepo(dbHelper: DbHelper): IRepo<ExchangeRate> = BaseCache(ExchangeRateRepo(dbHelper))

    @Provides
    @Singleton
    fun providesRecordRepo(dbHelper: DbHelper): IRepo<Record> = BaseCache(RecordRepo(dbHelper))

    @Provides
    @Singleton
    fun providesTransferRepo(dbHelper: DbHelper): IRepo<Transfer> = BaseCache(TransferRepo(dbHelper))
}
