package com.blogspot.e_kanivets.moneytracker.repo.base

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.base.IEntity
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import timber.log.Timber

/**
 * Base implementation of [IRepo].
 * No need to call db.close() at all, because SQLiteOpenHelper manage it for us + cache instances.
 * It will speed up all DB operations.
 * Created on 2/15/16.
 *
 * @author Evgenii Kanivets
 */
abstract class BaseRepo<T : IEntity>(protected val dbHelper: DbHelper) : IRepo<T> {

    protected abstract fun getTable(): String
    protected abstract fun contentValues(instance: T?): ContentValues?
    protected abstract fun getListFromCursor(cursor: Cursor?): List<T>

    override fun create(instance: T?): T? {
        val db = dbHelper.writableDatabase
        val id = db.insert(getTable(), null, contentValues(instance))

        return if (id == -1L) {
            Timber.d("Couldn't create record: %s", instance)
            null
        } else {
            val createdInstance = read(id)
            Timber.d("Created record: %s", createdInstance)
            createdInstance
        }
    }

    override fun read(id: Long): T? {
        val list = readWithCondition("id=?", arrayOf(id.toString()))
        return if (list.size == 1) list[0] else null
    }

    override fun update(instance: T?): T? {
        if (instance == null) return null

        val db = dbHelper.writableDatabase
        val args = arrayOf(instance.id.toString())
        val rowsAffected = db.update(getTable(), contentValues(instance), "id=?", args)

        return if (rowsAffected == 0) {
            Timber.d("Couldn't update record: %s", instance)
            null
        } else {
            val updatedInstance = read(instance.id)
            Timber.d("Updated record: %s", updatedInstance)
            updatedInstance
        }
    }

    override fun delete(instance: T?): Boolean {
        if (instance == null) return false

        val db = dbHelper.writableDatabase
        val args = arrayOf(instance.id.toString())
        val rowsAffected = db.delete(getTable(), "id=?", args)

        Timber.d("%s %s deleted", instance, if (rowsAffected == 0) " didn't " else " ")

        return rowsAffected != 0
    }

    override fun readAll(): List<T> = readWithCondition(null, null)

    override fun readWithCondition(condition: String?, args: Array<String?>?): List<T> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(getTable(), null, condition, args, null, null, null)
        val list = getListFromCursor(cursor)
        cursor.close()
        return list
    }
}
