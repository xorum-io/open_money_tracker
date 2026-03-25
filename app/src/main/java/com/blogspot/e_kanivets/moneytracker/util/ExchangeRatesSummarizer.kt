package com.blogspot.e_kanivets.moneytracker.util

import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate
import java.util.TreeMap

/**
 * Util class to summarize list of [ExchangeRate]s.
 * Returns only rates with unique pairs of currencies.
 * Created on 4/1/16.
 *
 * @author Evgenii Kanivets
 */
class ExchangeRatesSummarizer(private val rateList: List<ExchangeRate>) {

    /**
     * @return summary list in increasing order of createdAt values.
     */
    val summaryList: List<ExchangeRate> get() {
        val rateMap = TreeMap<String, ExchangeRate>()

        for (rate in rateList) {
            val ratePair = "${rate.fromCurrency}-${rate.toCurrency}"

            if (rateMap.containsKey(ratePair)) {
                val curRate = rateMap[ratePair]!!
                if (curRate.createdAt < rate.createdAt) rateMap[ratePair] = rate
            } else rateMap[ratePair] = rate
        }

        val summaryList = mutableListOf<ExchangeRate>()
        for (ratePair in rateMap.keys) {
            summaryList.add(rateMap[ratePair]!!)
        }

        summaryList.sortWith(Comparator { lhs, rhs ->
            if (lhs.createdAt < rhs.createdAt) -1
            else if (lhs.createdAt == rhs.createdAt) 0
            else 1
        })

        return summaryList
    }

    val pairedSummaryList: List<ExchangeRatePair> get() {
        val exchangeRatePairList = mutableListOf<ExchangeRatePair>()
        val rateMap = TreeMap<String, ExchangeRatePair>()

        val exchangeRateList = summaryList
        for (rate in exchangeRateList) {
            val ratePair = "${rate.fromCurrency}-${rate.toCurrency}"
            val reverseRatePair = "${rate.toCurrency}-${rate.fromCurrency}"

            if (rateMap.containsKey(ratePair)) {
                val pair = rateMap[ratePair]!!
                pair.secondRate = rate
            } else if (rateMap.containsKey(reverseRatePair)) {
                val pair = rateMap[reverseRatePair]!!
                pair.secondRate = rate
            } else {
                val pair = ExchangeRatePair()
                pair.firstRate = rate
                rateMap[ratePair] = pair
            }
        }

        for (ratePair in rateMap.keys) {
            val pair = rateMap[ratePair]!!
            if (pair.make()) exchangeRatePairList.add(pair)
        }

        return exchangeRatePairList
    }
}
