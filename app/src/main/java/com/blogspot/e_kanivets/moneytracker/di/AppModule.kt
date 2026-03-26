package com.blogspot.e_kanivets.moneytracker.di

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
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import com.blogspot.e_kanivets.moneytracker.repo.cache.BaseCache
import com.blogspot.e_kanivets.moneytracker.repo.data.AccountRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.CategoryRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.ExchangeRateRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.RecordRepo
import com.blogspot.e_kanivets.moneytracker.repo.data.TransferRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    // DB
    single { DbHelper(androidContext()) }

    // Repos
    single<IRepo<Account>> { BaseCache(AccountRepo(get())) }
    single<IRepo<Category>> { BaseCache(CategoryRepo(get())) }
    single<IRepo<ExchangeRate>> { BaseCache(ExchangeRateRepo(get())) }
    single<IRepo<Record>> { BaseCache(RecordRepo(get())) }
    single<IRepo<Transfer>> { BaseCache(TransferRepo(get())) }

    // Controllers
    single { PreferenceController(androidContext()) }
    single { AccountController(get(), get()) }
    single { CategoryController(get(), get()) }
    single { ExchangeRateController(get()) }
    single { RecordController(get(), get(), get(), get()) }
    single { TransferController(get(), get()) }
    single { CurrencyController(get(), get()) }
    single { PeriodController(get()) }
    single { FormatController(get()) }
    single { ExportController(get(), get()) }
    single { ImportController(get()) }
    single { BackupController(get(), androidContext().applicationInfo.dataDir) }
}
