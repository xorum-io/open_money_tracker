package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import junit.framework.TestCase
import org.mockito.Mockito

class AccountRepoTest : TestCase() {
    private lateinit var repo: AccountRepo

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        val mock = Mockito.mock(DbHelper::class.java)
        repo = AccountRepo(mock)
    }

    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Throws(Exception::class)
    fun testGetTable() {
        assertEquals(DbHelper.TABLE_ACCOUNTS, repo.getTable())
    }

    @Throws(Exception::class)
    fun testContentValues() {
        val account = Account(-1L, "title1", 100L, "NON", 30L, 0.0, false, 0)

        val expected = ContentValues()
        expected.put(DbHelper.TITLE_COLUMN, "title1")
        expected.put(DbHelper.CUR_SUM_COLUMN, 100L)
        expected.put(DbHelper.CURRENCY_COLUMN, "NON")
        expected.put(DbHelper.DECIMALS_COLUMN, 30L)
        expected.put(DbHelper.GOAL_COLUMN, 0.0)
        expected.put(DbHelper.ARCHIVED_COLUMN, false)
        expected.put(DbHelper.COLOR_COLUMN, 0)

        val actual = repo.contentValues(account)
        assertEquals(expected, actual)

        assertNull(repo.contentValues(null))
    }

    @Throws(Exception::class)
    fun testGetListFromCursor() {
        assertEquals(ArrayList<Account>(), repo.getListFromCursor(Mockito.mock(Cursor::class.java)))

        val mockCursor = Mockito.mock(Cursor::class.java)
        Mockito.`when`(mockCursor.moveToFirst()).thenReturn(true)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.TITLE_COLUMN)).thenReturn(2)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.CUR_SUM_COLUMN)).thenReturn(3)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.CURRENCY_COLUMN)).thenReturn(4)

        Mockito.`when`(mockCursor.getLong(1)).thenReturn(1L)
        Mockito.`when`(mockCursor.getString(2)).thenReturn("title")
        Mockito.`when`(mockCursor.getLong(3)).thenReturn(100L)
        Mockito.`when`(mockCursor.getString(4)).thenReturn("NON")

        val expected = listOf(Account(1L, "title", 100L, "NON", 0L, 0.0, false, 0))
        assertEquals(expected, repo.getListFromCursor(mockCursor))

        assertEquals(ArrayList<Account>(), repo.getListFromCursor(null))
    }
}
