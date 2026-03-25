package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import junit.framework.TestCase
import org.mockito.Mockito

class ExchangeRateRepoTest : TestCase() {
    private lateinit var repo: ExchangeRateRepo

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        val mock = Mockito.mock(DbHelper::class.java)
        repo = ExchangeRateRepo(mock)
    }

    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Throws(Exception::class)
    fun testGetTable() {
        assertEquals(DbHelper.TABLE_RATES, repo.getTable())
    }

    @Throws(Exception::class)
    fun testContentValues() {
        val rate = ExchangeRate(1L, 1L, "NON", "USD", 100.0)

        val expected = ContentValues()
        expected.put(DbHelper.CREATED_AT_COLUMN, 1L)
        expected.put(DbHelper.FROM_CURRENCY_COLUMN, "NON")
        expected.put(DbHelper.TO_CURRENCY_COLUMN, "USD")
        expected.put(DbHelper.AMOUNT_COLUMN, 100.0)

        val actual = repo.contentValues(rate)
        assertEquals(expected, actual)

        assertNull(repo.contentValues(null))
    }

    @Throws(Exception::class)
    fun testGetListFromCursor() {
        assertEquals(ArrayList<ExchangeRate>(), repo.getListFromCursor(Mockito.mock(Cursor::class.java)))

        val mockCursor = Mockito.mock(Cursor::class.java)
        Mockito.`when`(mockCursor.moveToFirst()).thenReturn(true)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.CREATED_AT_COLUMN)).thenReturn(2)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.FROM_CURRENCY_COLUMN)).thenReturn(3)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TO_CURRENCY_COLUMN)).thenReturn(4)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.AMOUNT_COLUMN)).thenReturn(5)

        Mockito.`when`(mockCursor.getLong(1)).thenReturn(1L)
        Mockito.`when`(mockCursor.getLong(2)).thenReturn(1L)
        Mockito.`when`(mockCursor.getString(3)).thenReturn("NON")
        Mockito.`when`(mockCursor.getString(4)).thenReturn("USD")
        Mockito.`when`(mockCursor.getDouble(5)).thenReturn(100.0)

        val expected = listOf(ExchangeRate(1L, 1L, "NON", "USD", 100.0))
        assertEquals(expected, repo.getListFromCursor(mockCursor))

        assertEquals(ArrayList<ExchangeRate>(), repo.getListFromCursor(null))
    }
}
