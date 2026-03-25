package com.blogspot.e_kanivets.moneytracker.controller.data

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class CategoryControllerTest {
    private lateinit var mockPrefs: PreferenceController

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockPrefs = Mockito.mock(PreferenceController::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testReadOrCreate() {
        var categoryController = CategoryController(emptyRepo, mockPrefs)

        assertNull(categoryController.readOrCreate(null))
        assertNull(categoryController.readOrCreate(""))

        var result = categoryController.readOrCreate("category 1")
        assertEquals("category 1", result?.name)

        result = categoryController.readOrCreate("category 1")
        assertEquals("category 1", result?.name)

        categoryController = CategoryController(repo, mockPrefs)

        result = categoryController.readOrCreate("category 1")
        assertEquals("category 1", result?.name)

        result = categoryController.readOrCreate("Category 1")
        assertEquals("Category 1", result?.name)
    }

    private val emptyRepo: IRepo<Category> = object : IRepo<Category> {
        override fun create(instance: Category?): Category? = instance
        override fun read(id: Long): Category? = null
        override fun update(instance: Category?): Category? = null
        override fun delete(instance: Category?): Boolean = false
        override fun readAll(): List<Category> = emptyList()
        override fun readWithCondition(condition: String?, args: Array<String?>?): List<Category> = emptyList()
    }

    private val repo: IRepo<Category> = object : IRepo<Category> {
        private val category = Category(1L, "Category 1")

        override fun create(instance: Category?): Category? = instance
        override fun read(id: Long): Category? = if (id == 1L) category else null
        override fun update(instance: Category?): Category? = null
        override fun delete(instance: Category?): Boolean = false
        override fun readAll(): List<Category> = listOf(category)
        override fun readWithCondition(condition: String?, args: Array<String?>?): List<Category> = emptyList()
    }
}
