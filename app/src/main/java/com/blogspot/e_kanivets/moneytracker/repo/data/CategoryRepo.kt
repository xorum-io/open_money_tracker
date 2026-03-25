package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import com.blogspot.e_kanivets.moneytracker.repo.base.BaseRepo
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * [IRepo] implementation for [Category] entity.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
class CategoryRepo(dbHelper: DbHelper) : BaseRepo<Category>(dbHelper) {

    override fun getTable(): String = DbHelper.TABLE_CATEGORIES

    override fun contentValues(instance: Category?): ContentValues? {
        if (instance == null) return null
        val contentValues = ContentValues()
        contentValues.put(DbHelper.NAME_COLUMN, instance.name)
        return contentValues
    }

    override fun getListFromCursor(cursor: Cursor?): List<Category> {
        val categoryList = mutableListOf<Category>()
        if (cursor == null) return categoryList

        if (cursor.moveToFirst()) {
            val idColIndex = cursor.getColumnIndex(DbHelper.ID_COLUMN)
            val nameColIndex = cursor.getColumnIndex(DbHelper.NAME_COLUMN)

            do {
                categoryList.add(Category(cursor.getLong(idColIndex), cursor.getString(nameColIndex)))
            } while (cursor.moveToNext())
        }

        return categoryList
    }
}
