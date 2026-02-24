package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import junit.framework.TestCase
import org.mockito.Mockito

class RecordRepoTest : TestCase() {
    private lateinit var repo: RecordRepo

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        val mock = Mockito.mock(DbHelper::class.java)
        repo = RecordRepo(mock)
    }

    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Throws(Exception::class)
    fun testGetTable() {
        assertEquals(DbHelper.TABLE_RECORDS, repo.getTable())
    }

    @Throws(Exception::class)
    fun testContentValues() {
        val record = Record(1L, 1L, Record.TYPE_EXPENSE, "title", 1L, null, 100L, 1L, "NON", 50L)

        val expected = ContentValues()
        expected.put(DbHelper.TIME_COLUMN, 1L)
        expected.put(DbHelper.TYPE_COLUMN, Record.TYPE_EXPENSE)
        expected.put(DbHelper.TITLE_COLUMN, "title")
        expected.put(DbHelper.CATEGORY_ID_COLUMN, 1L)
        expected.put(DbHelper.PRICE_COLUMN, 100L)
        expected.put(DbHelper.ACCOUNT_ID_COLUMN, 1L)
        expected.put(DbHelper.CURRENCY_COLUMN, "NON")
        expected.put(DbHelper.DECIMALS_COLUMN, 50L)

        val actual = repo.contentValues(record)
        assertEquals(expected, actual)

        assertNull(repo.contentValues(null))
    }

    @Throws(Exception::class)
    fun testGetListFromCursor() {
        assertEquals(ArrayList<Record>(), repo.getListFromCursor(Mockito.mock(Cursor::class.java)))

        val mockCursor = Mockito.mock(Cursor::class.java)
        Mockito.`when`(mockCursor.moveToFirst()).thenReturn(true)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TIME_COLUMN)).thenReturn(2)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TYPE_COLUMN)).thenReturn(3)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TITLE_COLUMN)).thenReturn(4)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.CATEGORY_ID_COLUMN)).thenReturn(5)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.PRICE_COLUMN)).thenReturn(6)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.ACCOUNT_ID_COLUMN)).thenReturn(7)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.CURRENCY_COLUMN)).thenReturn(8)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.DECIMALS_COLUMN)).thenReturn(9)

        Mockito.`when`(mockCursor.getLong(1)).thenReturn(1L)
        Mockito.`when`(mockCursor.getLong(2)).thenReturn(1L)
        Mockito.`when`(mockCursor.getInt(3)).thenReturn(Record.TYPE_EXPENSE)
        Mockito.`when`(mockCursor.getString(4)).thenReturn("title")
        Mockito.`when`(mockCursor.getLong(5)).thenReturn(1L)
        Mockito.`when`(mockCursor.getLong(6)).thenReturn(100L)
        Mockito.`when`(mockCursor.getLong(7)).thenReturn(1L)
        Mockito.`when`(mockCursor.getString(8)).thenReturn("NON")
        Mockito.`when`(mockCursor.getLong(9)).thenReturn(50L)

        val expected = listOf(Record(1L, 1L, Record.TYPE_EXPENSE, "title", 1L, null, 100L, 1L, "NON", 50L))
        assertEquals(expected, repo.getListFromCursor(mockCursor))

        assertEquals(ArrayList<Record>(), repo.getListFromCursor(null))
    }
}
