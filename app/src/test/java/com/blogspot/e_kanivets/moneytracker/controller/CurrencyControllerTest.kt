package com.blogspot.e_kanivets.moneytracker.controller

import com.blogspot.e_kanivets.moneytracker.controller.data.AccountController
import com.blogspot.e_kanivets.moneytracker.entity.data.Account
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class CurrencyControllerTest {

    @Test
    @Throws(Exception::class)
    fun testReadDefaultCurrency() {
        val accountMock = Mockito.mock(AccountController::class.java)
        val prefsMock = Mockito.mock(PreferenceController::class.java)
        val currencyController = CurrencyController(accountMock, prefsMock)

        Mockito.`when`(prefsMock.readDefaultCurrency()).thenReturn(null)
        Mockito.`when`(accountMock.readDefaultAccount()).thenReturn(null)
        assertEquals("NON", currencyController.readDefaultCurrency())

        val account = Account(1L, "a1", 100L, "ACM", 0L, 0.0, false, 0)
        Mockito.`when`(prefsMock.readDefaultCurrency()).thenReturn(null)
        Mockito.`when`(accountMock.readDefaultAccount()).thenReturn(account)
        assertEquals(account.currency, currencyController.readDefaultCurrency())

        Mockito.`when`(prefsMock.readDefaultCurrency()).thenReturn("SOS")
        Mockito.`when`(accountMock.readDefaultAccount()).thenReturn(account)
        assertEquals("SOS", currencyController.readDefaultCurrency())
    }
}
