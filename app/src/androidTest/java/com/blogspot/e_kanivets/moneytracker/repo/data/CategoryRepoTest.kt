package com.blogspot.e_kanivets.moneytracker.repo.data

import android.content.ContentValues
import android.database.Cursor
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.repo.DbHelper
import junit.framework.TestCase
import org.mockito.Mockito

class CategoryRepoTest : TestCase() {
    private lateinit var repo: CategoryRepo

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        val mock = Mockito.mock(DbHelper::class.java)
        repo = CategoryRepo(mock)
    }

    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Throws(Exception::class)
    fun testGetTable() {
        assertEquals(DbHelper.TABLE_CATEGORIES, repo.getTable())
    }

    @Throws(Exception::class)
    fun testContentValues() {
        val category = Category(1L, "category")

        val expected = ContentValues()
        expected.put(DbHelper.NAME_COLUMN, "category")

        val actual = repo.contentValues(category)
        assertEquals(expected, actual)

        assertNull(repo.contentValues(null))
    }

    @Throws(Exception::class)
    fun testGetListFromCursor() {
        assertEquals(ArrayList<Category>(), repo.getListFromCursor(Mockito.mock(Cursor::class.java)))

        val mockCursor = Mockito.mock(Cursor::class.java)
        Mockito.`when`(mockCursor.moveToFirst()).thenReturn(true)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.ID_COLUMN)).thenReturn(1)
        Mockito.`when`(mockCursor.getColumnIndex(DbHelper.NAME_COLUMN)).thenReturn(2)

        Mockito.`when`(mockCursor.getLong(1)).thenReturn(1L)
        Mockito.`when`(mockCursor.getString(2)).thenReturn("category")

        val expected = listOf(Category(1L, "category"))
        assertEquals(expected, repo.getListFromCursor(mockCursor))

        assertEquals(ArrayList<Category>(), repo.getListFromCursor(null))
    }
}
