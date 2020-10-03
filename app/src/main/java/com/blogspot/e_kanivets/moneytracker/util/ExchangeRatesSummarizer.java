package com.blogspot.e_kanivets.moneytracker.util;

import androidx.annotation.NonNull;

import com.blogspot.e_kanivets.moneytracker.entity.ExchangeRatePair;
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Util class to summarize list of {@link ExchangeRate}s.
 * Returns only rates with unique pairs of currencies.
 * Created on 4/1/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRatesSummarizer {
    private List<ExchangeRate> rateList;

    public ExchangeRatesSummarizer(@NonNull List<ExchangeRate> rateList) {
        this.rateList = rateList;
    }

    /**
     * @return summary list in increasing order of createdAt values.
     */
    @NonNull
    public List<ExchangeRate> getSummaryList() {
        Map<String, ExchangeRate> rateMap = new TreeMap<>();

        for (ExchangeRate rate : rateList) {
            String ratePair = rate.getFromCurrency() + "-" + rate.getToCurrency();

            if (rateMap.containsKey(ratePair)) {
                ExchangeRate curRate = rateMap.get(ratePair);
                if (curRate.getCreatedAt() < rate.getCreatedAt()) rateMap.put(ratePair, rate);
            } else rateMap.put(ratePair, rate);
        }

        List<ExchangeRate> summaryList = new ArrayList<>();

        for (String ratePair : rateMap.keySet()) {
            summaryList.add(rateMap.get(ratePair));
        }

        Collections.sort(summaryList, new Comparator<ExchangeRate>() {
            @Override
            public int compare(ExchangeRate lhs, ExchangeRate rhs) {
                return lhs.getCreatedAt() < rhs.getCreatedAt() ? -1
                        : (lhs.getCreatedAt() == rhs.getCreatedAt() ? 0 : 1);
            }
        });

        return summaryList;
    }

    @NonNull
    public List<ExchangeRatePair> getPairedSummaryList() {
        List<ExchangeRatePair> exchangeRatePairList = new ArrayList<>();
        Map<String, ExchangeRatePair> rateMap = new TreeMap<>();

        List<ExchangeRate> exchangeRateList = getSummaryList();
        for (ExchangeRate rate : exchangeRateList) {
            String ratePair = rate.getFromCurrency() + "-" + rate.getToCurrency();
            String reverseRatePair = rate.getToCurrency() + "-" + rate.getFromCurrency();

            if (rateMap.containsKey(ratePair)) {
                ExchangeRatePair pair = rateMap.get(ratePair);
                pair.setSecondRate(rate);
            } else if (rateMap.containsKey(reverseRatePair)) {
                ExchangeRatePair pair = rateMap.get(reverseRatePair);
                pair.setSecondRate(rate);
            } else {
                ExchangeRatePair pair = new ExchangeRatePair();
                pair.setFirstRate(rate);
                rateMap.put(ratePair, pair);
            }
        }

        for (String ratePair : rateMap.keySet()) {
            ExchangeRatePair pair = rateMap.get(ratePair);
            if (pair.make()) exchangeRatePairList.add(pair);
        }

        return exchangeRatePairList;
    }
}
