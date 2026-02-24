package com.blogspot.e_kanivets.moneytracker.controller.data

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.entity.Period
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.Date

class RecordControllerTest {
    private lateinit var recordController: RecordController
    private lateinit var categoryMock: CategoryController
    private lateinit var accountMock: AccountController
    private lateinit var preferenceMock: PreferenceController
    private lateinit var repo: TestRepo

    @Before
    @Throws(Exception::class)
    fun setUp() {
        repo = TestRepo()
        categoryMock = Mockito.mock(CategoryController::class.java)
        accountMock = Mockito.mock(AccountController::class.java)
        preferenceMock = Mockito.mock(PreferenceController::class.java)
        recordController = RecordController(repo, categoryMock, accountMock, preferenceMock)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun testCreate() {
        assertNull(recordController.create(null))
        Mockito.verify(categoryMock, Mockito.times(0)).readOrCreate(null)
        Mockito.verify(accountMock, Mockito.times(0)).recordAdded(null)

        val category = Category(1L, "c1")
        val account = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        val record = Record(1L, 1L, Record.TYPE_INCOME, "r1", category, null, 10.0, account, "NON")
        Mockito.`when`(categoryMock.readOrCreate(category.name)).thenReturn(category)

        val result = recordController.create(record)
        assertEquals(result, record)
        assertEquals(repo.read(record.id), record)
        Mockito.verify(categoryMock, Mockito.times(1)).readOrCreate(category.name)
        Mockito.verify(accountMock, Mockito.times(1)).recordAdded(record)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdate() {
        assertNull(recordController.update(null))
        Mockito.verify(categoryMock, Mockito.times(0)).readOrCreate(null)
        Mockito.verify(accountMock, Mockito.times(0)).recordAdded(null)

        val category = Category(1L, "c1")
        val account = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        val record = Record(1L, 1L, Record.TYPE_INCOME, "r1", category, null, 10.0, account, "NON")
        Mockito.`when`(categoryMock.readOrCreate(category.name)).thenReturn(category)

        val result = recordController.update(record)
        assertEquals(result, record)
        assertEquals(repo.read(record.id), record)
        Mockito.verify(categoryMock, Mockito.times(1)).readOrCreate(category.name)
        Mockito.verify(accountMock, Mockito.times(1)).recordUpdated(null, record)
    }

    @Test
    @Throws(Exception::class)
    fun testDelete() {
        assertFalse(recordController.delete(null))
        Mockito.verify(accountMock, Mockito.times(0)).recordDeleted(null)

        val category = Category(1L, "c1")
        val account = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        val record = Record(1L, 1L, Record.TYPE_INCOME, "r1", category, null, 10.0, account, "NON")

        assertFalse(recordController.delete(record))
        Mockito.verify(accountMock, Mockito.times(0)).recordDeleted(null)

        repo.create(record)
        Mockito.`when`(recordController.delete(record)).thenReturn(true)
        assertTrue(recordController.delete(record))
        Mockito.verify(accountMock, Mockito.times(2)).recordDeleted(record)
    }

    @Test
    @Throws(Exception::class)
    fun testRead() {
        assertNull(recordController.read(-1))

        val category = Category(1L, "c1")
        val account = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        val recordNotFull = Record(1L, 1L, Record.TYPE_INCOME, "r1", category.id, null, 10L, account.id, "NON", 0L)
        val record = Record(1L, 1L, Record.TYPE_INCOME, "r1", category, null, 10.0, account, "NON")

        repo.create(recordNotFull)
        Mockito.`when`(categoryMock.read(category.id)).thenReturn(category)
        Mockito.`when`(accountMock.read(account.id)).thenReturn(account)

        assertEquals(record, recordController.read(record.id))
        Mockito.verify(categoryMock, Mockito.times(1)).read(category.id)
        Mockito.verify(accountMock, Mockito.times(1)).read(account.id)
    }

    @Test
    @Throws(Exception::class)
    fun testReadAll() {
        assertEquals(repo.readAll(), recordController.readAll())
    }

    @Test
    @Throws(Exception::class)
    fun testReadWithCondition() {
        assertEquals(emptyList<Record>(), recordController.readWithCondition(null, null))
    }

    @Test
    @Throws(Exception::class)
    fun testGetRecordsForPeriod() {
        assertEquals(emptyList<Record>(), recordController.getRecordsForPeriod(
            Period(Date(), Date(), Period.TYPE_CUSTOM)))
    }

    private inner class TestRepo : IRepo<Record> {
        private val recordMap = mutableMapOf<Long, Record>()

        override fun create(instance: Record?): Record? {
            if (instance == null) return null
            recordMap[instance.id] = instance
            return instance
        }

        override fun read(id: Long): Record? = recordMap[id]

        override fun update(instance: Record?): Record? {
            if (instance == null) return null
            recordMap[instance.id] = instance
            return instance
        }

        override fun delete(instance: Record?): Boolean {
            if (instance == null) return false
            recordMap.remove(instance.id)
            return true
        }

        override fun readAll(): List<Record> {
            return recordMap.values.sortedBy { it.time }
        }

        override fun readWithCondition(condition: String?, args: Array<String?>?): List<Record> = readAll()
    }
}
