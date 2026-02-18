package com.blogspot.e_kanivets.moneytracker.entity.data

import android.os.Parcel
import android.os.Parcelable
import com.blogspot.e_kanivets.moneytracker.entity.base.BaseEntity

/**
 * Entity class to represent transfer operation between accounts.
 * Created on 2/10/16.
 *
 * @author Evgenii Kanivets
 */
class Transfer : BaseEntity, Parcelable {
    val time: Long
    val fromAccountId: Long
    val toAccountId: Long
    val fromAmount: Long
    val toAmount: Long
    val fromDecimals: Long
    val toDecimals: Long

    constructor(
        id: Long, time: Long, fromAccountId: Long, toAccountId: Long,
        fromAmount: Long, toAmount: Long, fromDecimals: Long, toDecimals: Long
    ) {
        this.fromDecimals = fromDecimals
        this.toDecimals = toDecimals
        this.id = id
        this.time = time
        this.fromAccountId = fromAccountId
        this.toAccountId = toAccountId
        this.fromAmount = fromAmount
        this.toAmount = toAmount
    }

    constructor(time: Long, fromAccountId: Long, toAccountId: Long, fromAmount: Double, toAmount: Double) {
        this.time = time
        this.fromAccountId = fromAccountId
        this.toAccountId = toAccountId
        this.fromAmount = getLong(fromAmount)
        this.fromDecimals = getDecimal(fromAmount)
        this.toAmount = getLong(toAmount)
        this.toDecimals = getDecimal(toAmount)
    }

    constructor(parcel: Parcel) {
        time = parcel.readLong()
        fromAccountId = parcel.readLong()
        toAccountId = parcel.readLong()
        fromAmount = parcel.readLong()
        toAmount = parcel.readLong()
        fromDecimals = parcel.readLong()
        toDecimals = parcel.readLong()
    }

    val fullFromAmount: Double get() = fromAmount + fromDecimals / 100.0

    val fullToAmount: Double get() = toAmount + toDecimals / 100.0

    override fun equals(other: Any?): Boolean {
        if (other is Transfer) {
            return this.id == other.id
                    && this.time == other.time
                    && this.fromAccountId == other.fromAccountId
                    && this.toAccountId == other.toAccountId
                    && this.fromAmount == other.fromAmount
                    && this.toAmount == other.toAmount
                    && this.fromDecimals == other.fromDecimals
                    && this.toDecimals == other.toDecimals
        }
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Transfer {")
        sb.append("id = ").append(id).append(", ")
        sb.append("time = ").append(time).append(", ")
        sb.append("fromAccountId = ").append(fromAccountId).append(", ")
        sb.append("toAccountId = ").append(toAccountId).append(", ")
        sb.append("fromAmount = ").append(fromAmount).append(", ")
        sb.append("toAmount = ").append(toAmount).append(", ")
        sb.append("fromDecimals = ").append(fromDecimals).append(", ")
        sb.append("toDecimals = ").append(toDecimals)
        sb.append("}")
        return sb.toString()
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(time)
        dest.writeLong(fromAccountId)
        dest.writeLong(toAccountId)
        dest.writeLong(fromAmount)
        dest.writeLong(toAmount)
        dest.writeLong(fromDecimals)
        dest.writeLong(toDecimals)
    }

    companion object CREATOR : Parcelable.Creator<Transfer> {
        override fun createFromParcel(parcel: Parcel): Transfer = Transfer(parcel)
        override fun newArray(size: Int): Array<Transfer?> = arrayOfNulls(size)
    }
}
