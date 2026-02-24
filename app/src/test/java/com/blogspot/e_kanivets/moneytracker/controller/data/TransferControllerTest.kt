package com.blogspot.e_kanivets.moneytracker.controller.data

import com.blogspot.e_kanivets.moneytracker.entity.data.Transfer
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito

class TransferControllerTest {

    @Test
    @Throws(Exception::class)
    fun testCreate() {
        val mock = Mockito.mock(AccountController::class.java)
        val transfer = Transfer(1L, 1L, 1L, 2L, 10L, 20L, 0L, 0L)

        Mockito.`when`(mock.transferDone(transfer)).thenReturn(true)
        Mockito.`when`(mock.transferDone(null)).thenReturn(false)

        val transferController = TransferController(repo, mock)

        assertEquals(transfer, transferController.create(transfer))
        Mockito.verify(mock, Mockito.times(1)).transferDone(transfer)

        assertNull(transferController.create(null))
        Mockito.verify(mock, Mockito.times(0)).transferDone(null)
    }

    private val repo: IRepo<Transfer> = object : IRepo<Transfer> {
        override fun create(instance: Transfer?): Transfer? = instance
        override fun read(id: Long): Transfer? = null
        override fun update(instance: Transfer?): Transfer? = null
        override fun delete(instance: Transfer?): Boolean = false
        override fun readAll(): List<Transfer> = emptyList()
        override fun readWithCondition(condition: String?, args: Array<String?>?): List<Transfer> = emptyList()
    }
}
