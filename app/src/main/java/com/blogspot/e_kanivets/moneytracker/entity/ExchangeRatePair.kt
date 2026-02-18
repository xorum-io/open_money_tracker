package com.blogspot.e_kanivets.moneytracker.entity

import android.os.Parcel
import android.os.Parcelable
import com.blogspot.e_kanivets.moneytracker.entity.data.ExchangeRate

/**
 * Not data entity that's used for {@link com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter}.
 * Created on 7/13/16.
 *
 * @author Evgenii Kanivets
 */
class ExchangeRatePair() : Parcelable {
    var firstRate: ExchangeRate? = null
    var secondRate: ExchangeRate? = null
    var fromCurrency: String? = null
    var toCurrency: String? = null
    var amountBuy: Double = 0.0
    var amountSell: Double = 0.0

    constructor(fromCurrency: String?, toCurrency: String?, amountBuy: Double, amountSell: Double) : this() {
        this.fromCurrency = fromCurrency
        this.toCurrency = toCurrency
        this.amountBuy = amountBuy
        this.amountSell = amountSell
    }

    constructor(parcel: Parcel) : this() {
        firstRate = parcel.readParcelable(ExchangeRate::class.java.classLoader)
        secondRate = parcel.readParcelable(ExchangeRate::class.java.classLoader)
        fromCurrency = parcel.readString()
        toCurrency = parcel.readString()
        amountBuy = parcel.readDouble()
        amountSell = parcel.readDouble()
    }

    fun make(): Boolean {
        if (firstRate == null) return false
        if (secondRate == null) {
            fromCurrency = firstRate!!.fromCurrency
            toCurrency = firstRate!!.toCurrency
            amountBuy = firstRate!!.amount
            amountSell = firstRate!!.amount
        } else {
            if (firstRate!!.id <= secondRate!!.id) {
                fromCurrency = firstRate!!.fromCurrency
                toCurrency = firstRate!!.toCurrency
                amountBuy = firstRate!!.amount
                amountSell = 1 / secondRate!!.amount
            } else {
                fromCurrency = secondRate!!.fromCurrency
                toCurrency = secondRate!!.toCurrency
                amountBuy = secondRate!!.amount
                amountSell = 1 / firstRate!!.amount
            }
        }
        return true
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(firstRate, flags)
        dest.writeParcelable(secondRate, flags)
        dest.writeString(fromCurrency)
        dest.writeString(toCurrency)
        dest.writeDouble(amountBuy)
        dest.writeDouble(amountSell)
    }

    companion object CREATOR : Parcelable.Creator<ExchangeRatePair> {
        override fun createFromParcel(parcel: Parcel): ExchangeRatePair = ExchangeRatePair(parcel)
        override fun newArray(size: Int): Array<ExchangeRatePair?> = arrayOfNulls(size)
    }
}
