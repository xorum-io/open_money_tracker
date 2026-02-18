package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * [IRepo] implementation for [Transfer] entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
class TransferRepo(dbHelper: DbHelper) : BaseRepo<Transfer>(dbHelper) {

    override fun getTable(): String = DbHelper.TABLE_TRANSFERS

    override fun contentValues(instance: Transfer?): ContentValues? {
        if (instance == null) return null
        val contentValues = ContentValues()
        contentValues.put(DbHelper.TIME_COLUMN, instance.time)
        contentValues.put(DbHelper.FROM_ACCOUNT_ID_COLUMN, instance.fromAccountId)
        contentValues.put(DbHelper.TO_ACCOUNT_ID_COLUMN, instance.toAccountId)
        contentValues.put(DbHelper.FROM_AMOUNT_COLUMN, instance.fromAmount)
        contentValues.put(DbHelper.TO_AMOUNT_COLUMN, instance.toAmount)
        contentValues.put(DbHelper.DECIMALS_FROM_COLUMN, instance.fromDecimals)
        contentValues.put(DbHelper.DECIMALS_TO_COLUMN, instance.toDecimals)
        return contentValues
    }

    override fun getListFromCursor(cursor: Cursor?): List<Transfer> {
        val list = mutableListOf<Transfer>()
        if (cursor == null) return list

        if (cursor.moveToFirst()) {
            val idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN)
            val timeColIndex = cursor.getColumnIndex(DbHelper.TIME_COLUMN)
            val fromAccountIdColIndex = cursor.getColumnIndex(DbHelper.FROM_ACCOUNT_ID_COLUMN)
            val toAccountIdColIndex = cursor.getColumnIndex(DbHelper.TO_ACCOUNT_ID_COLUMN)
            val fromAmountColIndex = cursor.getColumnIndex(DbHelper.FROM_AMOUNT_COLUMN)
            val toAmountColIndex = cursor.getColumnIndex(DbHelper.TO_AMOUNT_COLUMN)
            val decimalsFromColIndex = cursor.getColumnIndex(DbHelper.DECIMALS_FROM_COLUMN)
            val decimalsToColIndex = cursor.getColumnIndex(DbHelper.DECIMALS_TO_COLUMN)

            do {
                list.add(
                    Transfer(
                        cursor.getLong(idColIndex),
                        cursor.getLong(timeColIndex),
                        cursor.getLong(fromAccountIdColIndex),
                        cursor.getLong(toAccountIdColIndex),
                        cursor.getLong(fromAmountColIndex),
                        cursor.getLong(toAmountColIndex),
                        cursor.getLong(decimalsFromColIndex),
                        cursor.getLong(decimalsToColIndex)
                    )
                )
            } while (cursor.moveToNext())
        }

        return list
    }
}
