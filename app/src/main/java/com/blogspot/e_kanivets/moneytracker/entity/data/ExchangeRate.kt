package com.blogspot.e_kanivets.moneytracker.entity.data

import android.os.Parcel
import android.os.Parcelable
import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity

/**
 * Entity class to represent exchange rate between two currencies.
 * Created on 2/23/16.
 *
 * @author Evgenii Kanivets
 */
class ExchangeRate : BaseEntity, Parcelable {
    val createdAt: Long
    val fromCurrency: String?
    val toCurrency: String?
    val amount: Double

    constructor(id: Long, createdAt: Long, fromCurrency: String?, toCurrency: String?, amount: Double) {
        this.id = id
        this.createdAt = createdAt
        this.fromCurrency = fromCurrency
        this.toCurrency = toCurrency
        this.amount = amount
    }

    constructor(createdAt: Long, fromCurrency: String?, toCurrency: String?, amount: Double) {
        this.id = -1L
        this.createdAt = createdAt
        this.fromCurrency = fromCurrency
        this.toCurrency = toCurrency
        this.amount = amount
    }

    constructor(parcel: Parcel) {
        createdAt = parcel.readLong()
        fromCurrency = parcel.readString()
        toCurrency = parcel.readString()
        amount = parcel.readDouble()
    }

    override fun equals(other: Any?): Boolean {
        if (other is ExchangeRate) {
            return this.id == other.id
                    && this.createdAt == other.createdAt
                    && equals(fromCurrency, other.fromCurrency)
                    && equals(this.toCurrency, other.toCurrency)
                    && this.amount == other.amount
        }
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("ExchangeRate {")
        sb.append("id = ").append(id).append(", ")
        sb.append("createdAt = ").append(createdAt).append(", ")
        sb.append("fromCurrency = ").append(fromCurrency).append(", ")
        sb.append("toCurrency = ").append(toCurrency).append(", ")
        sb.append("amount = ").append(amount)
        sb.append("}")
        return sb.toString()
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(createdAt)
        dest.writeString(fromCurrency)
        dest.writeString(toCurrency)
        dest.writeDouble(amount)
    }

    companion object CREATOR : Parcelable.Creator<ExchangeRate> {
        override fun createFromParcel(parcel: Parcel): ExchangeRate = ExchangeRate(parcel)
        override fun newArray(size: Int): Array<ExchangeRate?> = arrayOfNulls(size)
    }
}
