package com.blogspot.e_kanivets.moneytracker.controller.data

import com.blogspot.e_kanivets.moneytracker.controller.PreferenceController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import com.blogspot.e_kanivets.moneytracker.entity.data.Category
import com.blogspot.e_kanivets.moneytracker.entity.data.Record
import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class AccountControllerTest {
    private lateinit var accountController: AccountController
    private lateinit var mockPrefs: PreferenceController
    private lateinit var repo: TestRepo

    @Before
    @Throws(Exception::class)
    fun setUp() {
        repo = TestRepo()
        mockPrefs = Mockito.mock(PreferenceController::class.java)
        accountController = AccountController(repo, mockPrefs)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    @Throws(Exception::class)
    fun testRecordAdded() {
        val category = Category(1L, "c1")
        val account = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        repo.create(account)

        val income = Record(1L, 1L, Record.TYPE_INCOME, "income", category, null, 10.0, account, "NON")
        accountController.recordAdded(income)
        assertEquals(110L, account.curSum)

        val expense = Record(1L, 1L, Record.TYPE_EXPENSE, "expense", category, null, 10.0, account, "NON")
        accountController.recordAdded(expense)
        assertEquals(100L, account.curSum)

        accountController.recordAdded(null)
        assertEquals(100L, account.curSum)

        val broken = Record(1L, 1L, -1, "expense", category, null, 10.0, account, "NON")
        accountController.recordAdded(broken)
        assertEquals(100L, account.curSum)
    }

    @Test
    @Throws(Exception::class)
    fun testRecordDeleted() {
        val category = Category(1L, "c1")
        val account = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        repo.create(account)

        val income = Record(1L, 1L, Record.TYPE_INCOME, "income", category, null, 10.0, account, "NON")
        accountController.recordDeleted(income)
        assertEquals(90L, account.curSum)

        val expense = Record(1L, 1L, Record.TYPE_EXPENSE, "expense", category, null, 10.0, account, "NON")
        accountController.recordDeleted(expense)
        assertEquals(100L, account.curSum)

        accountController.recordAdded(null)
        assertEquals(100L, account.curSum)

        val broken = Record(1L, 1L, -1, "expense", category, null, 10.0, account, "NON")
        accountController.recordAdded(broken)
        assertEquals(100L, account.curSum)
    }

    @Test
    @Throws(Exception::class)
    fun testRecordUpdated() {
        val category = Category(1L, "c1")
        val account = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        repo.create(account)

        val incomeOld = Record(1L, 1L, Record.TYPE_INCOME, "income", category, null, 10.0, account, "NON")
        val incomeNew = Record(1L, 1L, Record.TYPE_INCOME, "income", category, null, 100.0, account, "NON")

        accountController.recordUpdated(incomeOld, incomeNew)
        assertEquals(190L, account.curSum)

        accountController.recordUpdated(incomeNew, incomeOld)
        assertEquals(100L, account.curSum)

        val expenseOld = Record(1L, 1L, Record.TYPE_EXPENSE, "expense", category, null, 10.0, account, "NON")
        val expenseNew = Record(1L, 1L, Record.TYPE_EXPENSE, "expense", category, null, 100.0, account, "NON")

        accountController.recordUpdated(expenseOld, expenseNew)
        assertEquals(10L, account.curSum)

        accountController.recordUpdated(expenseNew, expenseOld)
        assertEquals(100L, account.curSum)

        accountController.recordUpdated(null, null)
        assertEquals(100L, account.curSum)

        val broken = Record(1L, 1L, -1, "expense", category, null, 10.0, account, "NON")
        accountController.recordUpdated(broken, broken)
        assertEquals(100L, account.curSum)
    }

    @Test
    @Throws(Exception::class)
    fun testTransferDone() {
        val account1 = Account(1L, "a1", 100L, "NON", 0L, 0.0, false, 0)
        val account2 = Account(2L, "a2", 0L, "NON", 0L, 0.0, false, 0)

        repo.create(account1)
        repo.create(account2)

        accountController.transferDone(null)
        assertEquals(100L, account1.curSum)
        assertEquals(0L, account2.curSum)

        var transfer = Transfer(1L, 1L, account1.id, account2.id, 10L, 10L, 0L, 0L)
        accountController.transferDone(transfer)
        assertEquals(90L, account1.curSum)
        assertEquals(10L, account2.curSum)

        transfer = Transfer(2L, 1L, account1.id, account2.id, 10L, 10L, 0L, 0L)
        accountController.transferDone(transfer)
        assertEquals(80L, account1.curSum)
        assertEquals(20L, account2.curSum)

        transfer = Transfer(2L, 1L, account2.id, account1.id, 20L, 20L, 0L, 0L)
        accountController.transferDone(transfer)
        assertEquals(100L, account1.curSum)
        assertEquals(0L, account2.curSum)

        transfer = Transfer(2L, 1L, account1.id, account2.id, 0L, 100L, 0L, 0L)
        accountController.transferDone(transfer)
        assertEquals(100L, account1.curSum)
        assertEquals(100L, account2.curSum)
    }

    @Test
    @Throws(Exception::class)
    fun testReadDefaultAccount() {
        assertNull(accountController.readDefaultAccount())

        val account1 = Account(1L, "a1", 100L, "UAH", 0L, 0.0, false, 0)
        repo.create(account1)
        assertEquals(account1, accountController.readDefaultAccount())

        val account2 = Account(2L, "a2", 0L, "UAH", 0L, 0.0, false, 0)
        repo.create(account2)
        assertEquals(account1, accountController.readDefaultAccount())

        Mockito.`when`(mockPrefs.readDefaultAccountId()).thenReturn(2L)
        assertEquals(account2, accountController.readDefaultAccount())

        Mockito.`when`(mockPrefs.readDefaultAccountId()).thenReturn(-1L)
        assertEquals(account1, accountController.readDefaultAccount())
    }

    private inner class TestRepo : IRepo<Account> {
        private val accountMap = mutableMapOf<Long, Account>()

        override fun create(instance: Account?): Account? {
            if (instance == null) return null
            accountMap[instance.id] = instance
            return instance
        }

        override fun read(id: Long): Account? = accountMap[id]

        override fun update(instance: Account?): Account? {
            if (instance == null) return null
            accountMap[instance.id] = instance
            return instance
        }

        override fun delete(instance: Account?): Boolean {
            if (instance == null) return false
            accountMap.remove(instance.id)
            return true
        }

        override fun readAll(): List<Account> {
            return accountMap.values.sortedBy { it.title }
        }

        override fun readWithCondition(condition: String?, args: Array<String?>?): List<Account> = readAll()
    }
}
