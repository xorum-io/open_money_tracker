package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo

class AccountRepo(dbHelper: DbHelper) : BaseRepo<Account>(dbHelper) {

    public override fun getTable(): String = DbHelper.TABLE_ACCOUNTS

    public override fun contentValues(account: Account?): ContentValues? {
        if (account == null) return null
        return ContentValues().apply {
            put(DbHelper.TITLE_COLUMN, account.title)
            put(DbHelper.CUR_SUM_COLUMN, account.curSum)
            put(DbHelper.CURRENCY_COLUMN, account.currency)
            put(DbHelper.DECIMALS_COLUMN, account.decimals)
            put(DbHelper.GOAL_COLUMN, account.goal)
            put(DbHelper.ARCHIVED_COLUMN, account.isArchived)
            put(DbHelper.COLOR_COLUMN, account.color)
        }
    }

    public override fun getListFromCursor(cursor: Cursor?): MutableList<Account> {
        val accountList = mutableListOf<Account>()
        if (cursor == null) return accountList

        if (cursor.moveToFirst()) {
            val idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN)
            val titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN)
            val curSumColIndex = cursor.getColumnIndex(DbHelper.CUR_SUM_COLUMN)
            val currencyColIndex = cursor.getColumnIndex(DbHelper.CURRENCY_COLUMN)
            val decimalsColIndex = cursor.getColumnIndex(DbHelper.DECIMALS_COLUMN)
            val goalColIndex = cursor.getColumnIndex(DbHelper.GOAL_COLUMN)
            val archivedIndex = cursor.getColumnIndex(DbHelper.ARCHIVED_COLUMN)
            val colorIndex = cursor.getColumnIndex(DbHelper.COLOR_COLUMN)

            do {
                val account = Account(
                    cursor.getLong(idColIndex),
                    cursor.getString(titleColIndex),
                    cursor.getLong(curSumColIndex),
                    cursor.getString(currencyColIndex),
                    cursor.getLong(decimalsColIndex),
                    cursor.getDouble(goalColIndex),
                    cursor.getInt(archivedIndex) != 0,
                    cursor.getInt(colorIndex)
                )
                accountList.add(account)
            } while (cursor.moveToNext())
        }
        return accountList
    }
}
