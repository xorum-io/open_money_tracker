package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * [IRepo] implementation for [ExchangeRate] entity.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
class ExchangeRateRepo(dbHelper: DbHelper) : BaseRepo<ExchangeRate>(dbHelper) {

    override fun getTable(): String = DbHelper.TABLE_RATES

    override fun contentValues(instance: ExchangeRate?): ContentValues? {
        if (instance == null) return null
        val contentValues = ContentValues()
        contentValues.put(DbHelper.CREATED_AT_COLUMN, instance.createdAt)
        contentValues.put(DbHelper.FROM_CURRENCY_COLUMN, instance.fromCurrency)
        contentValues.put(DbHelper.TO_CURRENCY_COLUMN, instance.toCurrency)
        contentValues.put(DbHelper.AMOUNT_COLUMN, instance.amount)
        return contentValues
    }

    override fun getListFromCursor(cursor: Cursor?): List<ExchangeRate> {
        val list = mutableListOf<ExchangeRate>()
        if (cursor == null) return list

        if (cursor.moveToFirst()) {
            val idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN)
            val createdAtColIndex = cursor.getColumnIndex(DbHelper.CREATED_AT_COLUMN)
            val fromCurrencyColIndex = cursor.getColumnIndex(DbHelper.FROM_CURRENCY_COLUMN)
            val toCurrencyColIndex = cursor.getColumnIndex(DbHelper.TO_CURRENCY_COLUMN)
            val amountColIndex = cursor.getColumnIndex(DbHelper.AMOUNT_COLUMN)

            do {
                list.add(
                    ExchangeRate(
                        cursor.getLong(idColIndex),
                        cursor.getLong(createdAtColIndex),
                        cursor.getString(fromCurrencyColIndex),
                        cursor.getString(toCurrencyColIndex),
                        cursor.getDouble(amountColIndex)
                    )
                )
            } while (cursor.moveToNext())
        }

        return list
    }
}
