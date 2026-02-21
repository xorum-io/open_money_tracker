package com.blogspot.e_kanivets.moneytracker.controller.data

import com.blogspot.e_kanivets.moneytracker.controller.base.BaseController
import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import com.blogspot.e_kanivets.moneytracker.repo.base.IRepo

/**
 * Controller class to encapsulate exchange rates handling logic.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
class ExchangeRateController(repo: IRepo<ExchangeRate>) : BaseController<ExchangeRate>(repo) {

    fun deleteExchangeRatePair(pair: ExchangeRatePair?) {
        if (pair == null) return

        val rateListToRemove = mutableListOf<ExchangeRate>()
        for (rate in readAll()) {
            if (rate.fromCurrency == pair.fromCurrency && rate.toCurrency == pair.toCurrency)
                rateListToRemove.add(rate)
            if (rate.fromCurrency == pair.toCurrency && rate.toCurrency == pair.fromCurrency)
                rateListToRemove.add(rate)
        }

        for (rate in rateListToRemove) {
            delete(rate)
        }
    }

    fun createExchangeRatePair(pair: ExchangeRatePair?): ExchangeRatePair? {
        if (pair == null) return null

        // DON'T change the order, it may affect the order of exchange rate pair in Exchange Rates screen
        val exchangeRate = ExchangeRate(
            System.currentTimeMillis(),
            pair.fromCurrency, pair.toCurrency, pair.amountBuy
        )
        val exchangeRateReverse = ExchangeRate(
            System.currentTimeMillis(),
            pair.toCurrency, pair.fromCurrency, 1 / pair.amountSell
        )

        val createdRate = create(exchangeRate)
        val createdReverseRate = create(exchangeRateReverse)

        return if (createdRate == null || createdReverseRate == null) null else pair
    }
}
