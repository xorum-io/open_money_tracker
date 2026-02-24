package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import junit.framework.TestCase
import org.mockito.Mockito

class TransferRepoTest : TestCase() {
    private lateinit var repo: TransferRepo

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        val mock = Mockito.mock(DbHelper::class.java)
        repo = TransferRepo(mock)
    }

    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Throws(Exception::class)
    fun testGetTable() {
        assertEquals(DbHelper.TABLE_TRANSFERS, repo.getTable())
    }

    @Throws(Exception::class)
    fun testContentValues() {
        val transfer = Transfer(1L, 1L, 1L, 2L, 100L, 200L, 45L, 50L)

        val expected = ContentValues()
        expected.put(DbHelper.TIME_COLUMN, 1L)
        expected.put(DbHelper.FROM_ACCOUNT_ID_COLUMN, 1L)
        expected.put(DbHelper.TO_ACCOUNT_ID_COLUMN, 2L)
        expected.put(DbHelper.FROM_AMOUNT_COLUMN, 100L)
        expected.put(DbHelper.TO_AMOUNT_COLUMN, 200L)
        expected.put(DbHelper.DECIMALS_FROM_COLUMN, 45L)
        expected.put(DbHelper.DECIMALS_TO_COLUMN, 50L)

        val actual = repo.contentValues(transfer)
        assertEquals(expected, actual)

        assertNull(repo.contentValues(null))
    }

    @Throws(Exception::class)
    fun testGetListFromCursor() {
        assertEquals(ArrayList<Transfer>(), repo.getListFromCursor(Mockito.mock(Cursor::class.java)))

        val mockCursor = Mockito.mock(Cursor::class.java)
        Mockito.`when`(mockCursor.moveToFirst()).thenReturn(true)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TIME_COLUMN)).thenReturn(2)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.FROM_ACCOUNT_ID_COLUMN)).thenReturn(3)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TO_ACCOUNT_ID_COLUMN)).thenReturn(4)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.FROM_AMOUNT_COLUMN)).thenReturn(5)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TO_AMOUNT_COLUMN)).thenReturn(6)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.DECIMALS_FROM_COLUMN)).thenReturn(7)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.DECIMALS_TO_COLUMN)).thenReturn(8)

        Mockito.`when`(mockCursor.getLong(1)).thenReturn(1L)
        Mockito.`when`(mockCursor.getLong(2)).thenReturn(1L)
        Mockito.`when`(mockCursor.getLong(3)).thenReturn(1L)
        Mockito.`when`(mockCursor.getLong(4)).thenReturn(2L)
        Mockito.`when`(mockCursor.getLong(5)).thenReturn(100L)
        Mockito.`when`(mockCursor.getLong(6)).thenReturn(200L)
        Mockito.`when`(mockCursor.getLong(7)).thenReturn(45L)
        Mockito.`when`(mockCursor.getLong(8)).thenReturn(50L)

        val expected = listOf(Transfer(1L, 1L, 1L, 2L, 100L, 200L, 45L, 50L))
        assertEquals(expected, repo.getListFromCursor(mockCursor))

        assertEquals(ArrayList<Transfer>(), repo.getListFromCursor(null))
    }
}
