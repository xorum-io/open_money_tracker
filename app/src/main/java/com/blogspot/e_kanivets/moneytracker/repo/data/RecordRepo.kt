package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * [IRepo] implementation for [Record] entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
class RecordRepo(dbHelper: DbHelper) : BaseRepo<Record>(dbHelper) {

    override fun getTable(): String = DbHelper.TABLE_RECORDS

    override fun contentValues(instance: Record?): ContentValues? {
        if (instance == null || instance.category == null || instance.account == null) return null
        val contentValues = ContentValues()
        contentValues.put(DbHelper.TIME_COLUMN, instance.time)
        contentValues.put(DbHelper.TYPE_COLUMN, instance.type)
        contentValues.put(DbHelper.TITLE_COLUMN, instance.title)
        contentValues.put(DbHelper.CATEGORY_ID_COLUMN, instance.category.id)
        contentValues.put(DbHelper.NOTES_COLUMN, instance.notes)
        contentValues.put(DbHelper.PRICE_COLUMN, instance.price)
        contentValues.put(DbHelper.ACCOUNT_ID_COLUMN, instance.account.id)
        contentValues.put(DbHelper.CURRENCY_COLUMN, instance.currency)
        contentValues.put(DbHelper.DECIMALS_COLUMN, instance.decimals)
        return contentValues
    }

    override fun getListFromCursor(cursor: Cursor?): List<Record> {
        val recordList = mutableListOf<Record>()
        if (cursor == null) return recordList

        if (cursor.moveToFirst()) {
            val idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN)
            val timeColIndex = cursor.getColumnIndex(DbHelper.TIME_COLUMN)
            val typeColIndex = cursor.getColumnIndex(DbHelper.TYPE_COLUMN)
            val titleColIndex = cursor.getColumnIndex(DbHelper.TITLE_COLUMN)
            val categoryColIndex = cursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN)
            val notesColIndex = cursor.getColumnIndex(DbHelper.NOTES_COLUMN)
            val priceColIndex = cursor.getColumnIndex(DbHelper.PRICE_COLUMN)
            val accountIdColIndex = cursor.getColumnIndex(DbHelper.ACCOUNT_ID_COLUMN)
            val currencyColIndex = cursor.getColumnIndex(DbHelper.CURRENCY_COLUMN)
            val decimalsColIndex = cursor.getColumnIndex(DbHelper.DECIMALS_COLUMN)

            do {
                recordList.add(
                    Record(
                        cursor.getLong(idColIndex),
                        cursor.getLong(timeColIndex),
                        cursor.getInt(typeColIndex),
                        cursor.getString(titleColIndex),
                        cursor.getLong(categoryColIndex),
                        cursor.getString(notesColIndex),
                        cursor.getLong(priceColIndex),
                        cursor.getLong(accountIdColIndex),
                        cursor.getString(currencyColIndex),
                        cursor.getLong(decimalsColIndex)
                    )
                )
            } while (cursor.moveToNext())
        }

        return recordList
    }
}
