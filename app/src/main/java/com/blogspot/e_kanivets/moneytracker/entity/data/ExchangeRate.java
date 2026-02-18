package com.blogspot.e_kanivets.moneytracker.entity.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity;

/**
 * Entity class to represent exchange rate between two currencies.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
public class ExchangeRate extends BaseEntity implements Parcelable {
    private long createdAt;
    private String fromCurrency;
    private String toCurrency;
    private double amount;

    public ExchangeRate(long id, long createdAt, String fromCurrency, String toCurrency, double amount) {
        setId(id);
        this.createdAt = createdAt;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    public ExchangeRate(long createdAt, String fromCurrency, String toCurrency, double amount) {
        setId(-1L);
        this.createdAt = createdAt;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    protected ExchangeRate(Parcel in) {
        createdAt = in.readLong();
        fromCurrency = in.readString();
        toCurrency = in.readString();
        amount = in.readDouble();
    }

    public static final Creator<ExchangeRate> CREATOR = new Creator<ExchangeRate>() {
        @Override
        public ExchangeRate createFromParcel(Parcel in) {
            return new ExchangeRate(in);
        }

        @Override
        public ExchangeRate[] newArray(int size) {
            return new ExchangeRate[size];
        }
    };

    public long getCreatedAt() {
        return createdAt;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExchangeRate) {
            ExchangeRate rate = (ExchangeRate) o;
            return this.getId() == rate.getId()
                    && this.createdAt == rate.getCreatedAt()
                    && equals(fromCurrency, rate.getFromCurrency())
                    && equals(this.toCurrency, rate.getToCurrency())
                    && this.amount == rate.getAmount();
        } else return false;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExchangeRate {");
        sb.append("id = ").append(getId()).append(", ");
        sb.append("createdAt = ").append(createdAt).append(", ");
        sb.append("fromCurrency = ").append(fromCurrency).append(", ");
        sb.append("toCurrency = ").append(toCurrency).append(", ");
        sb.append("amount = ").append(amount);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createdAt);
        dest.writeString(fromCurrency);
        dest.writeString(toCurrency);
        dest.writeDouble(amount);
    }
}