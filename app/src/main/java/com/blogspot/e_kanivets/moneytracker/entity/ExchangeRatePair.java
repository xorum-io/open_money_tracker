package com.blogspot.e_kanivets.moneytracker.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate;

/**
 * Not data entity that's used for {@link com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter}.
 * Created on 7/13/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRatePair implements Parcelable {
    @Nullable
    private ExchangeRate firstRate;
    @Nullable
    private ExchangeRate secondRate;

    private String fromCurrency;
    private String toCurrency;
    private double amountBuy;
    private double amountSell;

    public ExchangeRatePair() {
    }

    public ExchangeRatePair(String fromCurrency, String toCurrency, double amountBuy, double amountSell) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amountBuy = amountBuy;
        this.amountSell = amountSell;
    }

    protected ExchangeRatePair(Parcel in) {
        firstRate = in.readParcelable(ExchangeRate.class.getClassLoader());
        secondRate = in.readParcelable(ExchangeRate.class.getClassLoader());
        fromCurrency = in.readString();
        toCurrency = in.readString();
        amountBuy = in.readDouble();
        amountSell = in.readDouble();
    }

    public static final Creator<ExchangeRatePair> CREATOR = new Creator<ExchangeRatePair>() {
        @Override
        public ExchangeRatePair createFromParcel(Parcel in) {
            return new ExchangeRatePair(in);
        }

        @Override
        public ExchangeRatePair[] newArray(int size) {
            return new ExchangeRatePair[size];
        }
    };

    /**
     * Initialize String and primitive values from {@link ExchangeRate} instances provided.
     * @return true if initialized, false otherwise
     */
    public boolean make() {
        if (firstRate == null) return false;
        if (secondRate == null) {
            fromCurrency = firstRate.getFromCurrency();
            toCurrency = firstRate.getToCurrency();
            amountBuy = firstRate.getAmount();
            amountSell = firstRate.getAmount();
        } else {
            if (firstRate.getId() <= secondRate.getId()) {
                fromCurrency = firstRate.getFromCurrency();
                toCurrency = firstRate.getToCurrency();
                amountBuy = firstRate.getAmount();
                amountSell = 1 / secondRate.getAmount();
            } else {
                fromCurrency = secondRate.getFromCurrency();
                toCurrency = secondRate.getToCurrency();
                amountBuy = secondRate.getAmount();
                amountSell = 1 / firstRate.getAmount();
            }
        }
        return true;
    }

    public void setFirstRate(@Nullable ExchangeRate firstRate) {
        this.firstRate = firstRate;
    }

    public void setSecondRate(@Nullable ExchangeRate secondRate) {
        this.secondRate = secondRate;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmountBuy() {
        return amountBuy;
    }

    public double getAmountSell() {
        return amountSell;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(firstRate, flags);
        dest.writeParcelable(secondRate, flags);
        dest.writeString(fromCurrency);
        dest.writeString(toCurrency);
        dest.writeDouble(amountBuy);
        dest.writeDouble(amountSell);
    }
}
